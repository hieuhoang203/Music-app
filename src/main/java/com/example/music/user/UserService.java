package com.example.music.user;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.music.common.ComboboxValue;
import com.example.music.common.Constant;
import com.example.music.common.Message;
import com.example.music.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    /*
    * params is the path to save the user's photos to the cloud
    * cloudinary used to save user photos to the cloud
    * */
    private static final Map params = ObjectUtils.asMap(
            "folder", "avatar",
            "resource_type", "image"
    );

    private final Cloudinary cloudinary;

    /*
    * userRepository used for creating new users
    * accountRepository used for creating accounts associated with newly created users
    * */
    private final UserRepository userRepository;

    /*
    * simpleDateFormat use reformat date
    * */
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private final PasswordEncoder passwordEncoder;

    public Map<Object, Object> verifyUser(Byte type, UserDto dto) {
        Map<Object, Object> finalResult = new HashMap<>();
        Result result = Result.OK();
        Boolean check = true;
        try {

            if (dto.getName().isEmpty()) {
                result = new Result(Message.INVALID_USERNAME.getCode(), false, Message.INVALID_USERNAME.getMessage());
                check = false;
            }

            if (dto.getBirthday().isEmpty()) {
                result = new Result(Message.INVALID_DATE_OF_BIRTH.getCode(), false, Message.INVALID_DATE_OF_BIRTH.getMessage());
                check = false;
            }

            if (dto.getGender() == null) {
                result = new Result(Message.INVALID_GENDER.getCode(), false, Message.INVALID_GENDER.getMessage());
                check = false;
            }

            if (type == 2 && dto.getAvatar().isEmpty()) {
                result = new Result(Message.PHOTO_CANNOT_BE_BLANK.getCode(), false, Message.INVALID_PASSWORD.getMessage());
                check = false;
            }

            if (dto.getEmail().isEmpty() || userRepository.getAllEmail(dto.getEmail()) == 1) {
                result = new Result(Message.EMAIL_USER_EXIST.getCode(), false, Message.EMAIL_USER_EXIST.getMessage());
                check = false;
            }

            result = new Result(Message.INVALID_PERMISSION.getCode(), false, Message.INVALID_PERMISSION.getMessage());
            check = false;

            if (check) {
                finalResult.put(Constant.RESPONSE_KEY.DATA, dto);
            } else {
                finalResult.put(Constant.RESPONSE_KEY.DATA, null);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi xác thực thông tin người dùng! {} " + e.getMessage());
            result = new Result(Message.UNABLE_TO_VERIFY_INFORMATION.getCode(), false, Message.UNABLE_TO_VERIFY_INFORMATION.getMessage());
        }
        finalResult.put(Constant.RESPONSE_KEY.RESULT, result);
        return finalResult;
    }

    public Map<Object, Object> detailUser(String id) {
        Map<Object, Object> finalResult = new HashMap<>();
        Result result = Result.OK();
        try {
            Optional<User> user = userRepository.findById(id);
            if (!user.isEmpty()) {
                UserDto dto = UserDto.builder()
                        .id(user.get().getId())
                        .name(user.get().getName())
                        .gender(user.get().getGender())
                        .email(user.get().getLogin())
                        .birthday(user.get().getBirthday() != null ? user.get().getBirthday().toString() : user.get().getCreate_date().toString())
                        .role(user.get().getRole())
                        .build();
                finalResult.put(Constant.RESPONSE_KEY.DATA, dto);
            } else {
                finalResult.put(Constant.RESPONSE_KEY.DATA, new UserDto());
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy ra chi tiết người dùng! {} " + e.getMessage());
            result = new Result(Message.ERROR_RETRIEVING_USER_DETAILS.getCode(), false, Message.ERROR_RETRIEVING_USER_DETAILS.getMessage());
        }
        finalResult.put(Constant.RESPONSE_KEY.RESULT, result);
        return finalResult;
    }

    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public Map<Object, Object> insert(UserDto dto) throws IOException, ParseException {
        Map<Object, Object> finalResult = new HashMap<>();
        Result result = Result.OK();
        try {
            Map urlAvt = cloudinary.uploader().upload(dto.getAvatar().getBytes(), params);
            Random random = new Random(1000000);

            User user = User.builder()
                    .id(UUID.randomUUID().toString())
                    .name(dto.getName().trim())
                    .gender(Boolean.valueOf(dto.getGender()))
                    .login(dto.getEmail())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .birthday(new Date(simpleDateFormat.parse(dto.getBirthday()).getTime()))
                    .avatar(urlAvt.get("secure_url").toString())
                    .create_date(new Date(new java.util.Date().getTime()))
                    .status(Constant.Status.Activate)
                    .build();
            this.userRepository.save(user);
//            javaMailService.sendPassWord(dto.getEmail(), "User creation successful!", random.toString(), dto.getName());
        } catch (Exception e) {
            System.out.println("Lỗi khi thực hiện thêm mới người dùng ! {} " + e.getMessage());
            result = new Result(Message.CANNOT_ADD_NEW_USER.getCode(), false, Message.CANNOT_ADD_NEW_USER.getMessage());
            throw e;
        }
        finalResult.put(Constant.RESPONSE_KEY.RESULT, result);
        return finalResult;
    }

    public Map<Object, Object> changeStatusUser(String id, String status) {
        Map<Object, Object> finalResult = new HashMap<>();
        Result result = Result.OK();
        try {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                user.get().setStatus(status);
                userRepository.save(user.get());
            } else {
                System.out.println("Không tìm thấy người dùng có id là : " + id);
                result = new Result(Message.UNABLE_TO_UPDATE_USER_STATUS.getCode(), false, Message.UNABLE_TO_UPDATE_USER_STATUS.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi thực hiện cập nhật trạng thái người dùng! {} " + e.getMessage());
            result = new Result(Message.UNABLE_TO_UPDATE_USER_STATUS.getCode(), false, Message.UNABLE_TO_UPDATE_USER_STATUS.getMessage());
        }
        finalResult.put(Constant.RESPONSE_KEY.RESULT, result);
        return finalResult;
    }

    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public Map<Object, Object> update(String id, UserDto dto) throws Exception {
        Map<Object, Object> finalResult = new HashMap<>();
        Result result = Result.OK();
        try {
            User user = this.userRepository.findById(id).orElse(null);
            if (dto.getAvatar() != null) {
                if (user.getAvatar() != null) {
                    String urlCloudinary = user.getAvatar();
                    int start = urlCloudinary.indexOf("avatar/");
                    int end = urlCloudinary.lastIndexOf(".");
                    cloudinary.api().deleteResources(Arrays.asList(urlCloudinary.substring(start, end)),
                            ObjectUtils.asMap("type", "upload", "resource_type", "image"));
                    Map cloud = cloudinary.uploader().upload(dto.getAvatar().getBytes(), params);
                    user.setAvatar(cloud.get("secure_url").toString());
                } else {
                    Map cloud = cloudinary.uploader().upload(dto.getAvatar().getBytes(), params);
                    user.setAvatar(cloud.get("secure_url").toString());
                }
            }
            user.setRole(dto.getRole().trim().equalsIgnoreCase("Admin") ? Constant.Role.ADMIN
                    : dto.getRole().trim().equalsIgnoreCase("Artis") ? Constant.Role.ARTIS : Constant.Role.USER);
            user.setName(dto.getName().trim());
            user.setBirthday(new Date(simpleDateFormat.parse(dto.getBirthday()).getTime()));
            user.setUpdate_date(new Date(new java.util.Date().getTime()));
            user.setGender(dto.getGender());
            this.userRepository.save(user);
            finalResult.put(Constant.RESPONSE_KEY.DATA, user);
        } catch (Exception e) {
            System.out.println("Lỗi khi thực hiện cập nhật thông tin người dùng! {} " + e.getMessage());
            result = new Result(Message.SYSTEM_ERROR.getCode(), false, Message.SYSTEM_ERROR.getMessage());
            throw e;
        }
        finalResult.put(Constant.RESPONSE_KEY.RESULT, result);
        return finalResult;
    }

    public Map<Object, Object> getNewUserOrArtis(String role) {
        Map<Object, Object> finalResult = new HashMap<>();
        Result result = Result.OK();
        try {
            Map<String, UserResponse> userMap = this.userRepository.getNewUserOrArtis(role).stream().collect(Collectors.toMap(UserResponse::getId, Function.identity()));
            finalResult.put(Constant.RESPONSE_KEY.DATA, userMap);
        } catch (Exception e) {
            System.out.println("Lỗi khi thực hiện lấy ra danh sách người dùng mới !{} " + e.getMessage());
            result = new Result(Message.ERROR_WHILE_GETTING_NEW_USER_LIST.getCode(), false, Message.ERROR_WHILE_GETTING_NEW_USER_LIST.getMessage());
        }
        finalResult.put(Constant.RESPONSE_KEY.RESULT, result);
        return finalResult;
    }

    public Map<Object, Object> getAllUser(Pageable pageable) {
        Map<Object, Object> finalResult = new HashMap<>();
        Result result = Result.OK();
        try {
            Page<UserResponse> data = this.userRepository.getAllUser(pageable);
            finalResult.put(Constant.RESPONSE_KEY.DATA, data);
        } catch (Exception e) {
            System.out.println("Lỗi khi thực hiện lấy ra danh sách người dùng !{} " + e.getMessage());
            result = new Result(Message.ERROR_WHILE_GETTING_USER_LIST.getCode(), false, Message.ERROR_WHILE_GETTING_USER_LIST.getMessage());
        }
        finalResult.put(Constant.RESPONSE_KEY.RESULT, result);
        return finalResult;
    }

    public Map<Object, Object> getUserByStatus(String status, Pageable pageable) {
        Map<Object, Object> finalResult = new HashMap<>();
        Result result = Result.OK();
        try {
            Page<UserResponse> data = this.userRepository.getUserByStatus(status, pageable);
            finalResult.put(Constant.RESPONSE_KEY.DATA, data);
        } catch (Exception e) {
            System.out.println("Lỗi khi thực hiện lấy ra danh sách người dùng !{} " + e.getMessage());
            result = new Result(Message.ERROR_WHILE_GETTING_USER_LIST.getCode(), false, Message.ERROR_WHILE_SEARCHING_FOR_SONGS.getMessage());
        }
        finalResult.put(Constant.RESPONSE_KEY.RESULT, result);
        return finalResult;
    }

    public Map<Object, Object> getAllArtis(Pageable pageable) {
        Map<Object, Object> finalResult = new HashMap<>();
        Result result = Result.OK();
        try {
            Page<ArtisResponse> data = this.userRepository.getAllArtis(pageable);
            finalResult.put(Constant.RESPONSE_KEY.DATA, data);
        } catch (Exception e) {
            System.out.println("Lỗi khi thực hiện lấy danh sách tác giả !{} " + e.getMessage());
            result = new Result(Message.ERROR_GETTING_AUTHOR_LIST.getCode(), false, Message.ERROR_GETTING_AUTHOR_LIST.getMessage());
        }
        finalResult.put(Constant.RESPONSE_KEY.RESULT, result);
        return finalResult;
    }

    public Map<Object, Object> getArtisByStatus(String status, Pageable pageable) {
        Map<Object, Object> finalResult = new HashMap<>();
        Result result = Result.OK();
        try {
            Page<ArtisResponse> data = this.userRepository.getArtisByStatus(status, pageable);
            finalResult.put(Constant.RESPONSE_KEY.DATA, data);
        } catch (Exception e) {
            System.out.println("Lỗi khi thực hiện lấy danh sách tác giả !{} " + e.getMessage());
            result = new Result(Message.ERROR_GETTING_AUTHOR_LIST.getCode(), false, Message.ERROR_GETTING_AUTHOR_LIST.getMessage());
        }
        finalResult.put(Constant.RESPONSE_KEY.RESULT, result);
        return finalResult;
    }

    public Map<Object, Object> updateStatusUser(String id, String account, String status) {
        Map<Object, Object> finalResult = new HashMap<>();
        Result result = Result.OK();
        try {
            this.userRepository.updateStatusUser(id, status);
            finalResult.put(Constant.RESPONSE_KEY.DATA, this.userRepository.findById(id).orElse(null));
        } catch (Exception e) {
            System.out.println("Lỗi khi thực hiện cập nhật trạng thái người dùng! {} " + e.getMessage());
        }
        finalResult.put(Constant.RESPONSE_KEY.RESULT, result);
        return finalResult;
    }

    public Map<Object, Object> getArtisForSelect() {
        Map<Object, Object> finalResult = new HashMap<>();
        Result result = Result.OK();
        try {
            List<ComboboxValue> data = this.userRepository.getArtisForSelect();
            finalResult.put(Constant.RESPONSE_KEY.DATA, data);
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy ra data tác giả trong select! {} " + e.getMessage());
            result = new Result(Message.ERROR_WHEN_GETTING_AUTHOR_DATA_TO_FILL_IN_SELECT_BOX.getCode(), false, Message.ERROR_WHEN_GETTING_AUTHOR_DATA_TO_FILL_IN_SELECT_BOX.getMessage());
        }
        finalResult.put(Constant.RESPONSE_KEY.RESULT, result);
        return finalResult;
    }

    public Map<Object, Object> getEmailUser() {
        Map<Object, Object> finalResult = new HashMap<>();
        Result result = Result.OK();
        try {
            List<String> data = this.userRepository.getEmailUser();;
            finalResult.put(Constant.RESPONSE_KEY.DATA, data);
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy ra data tác giả trong select! {} " + e.getMessage());
            result = new Result(Message.ERROR_WHEN_GETTING_AUTHOR_DATA_TO_FILL_IN_SELECT_BOX.getCode(), false, Message.ERROR_WHEN_GETTING_AUTHOR_DATA_TO_FILL_IN_SELECT_BOX.getMessage());
        }
        finalResult.put(Constant.RESPONSE_KEY.RESULT, result);
        return finalResult;
    }

}
