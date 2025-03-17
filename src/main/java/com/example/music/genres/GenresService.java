package com.example.music.genres;

import com.example.music.common.ComboboxValue;
import com.example.music.common.Constant;
import com.example.music.common.Message;
import com.example.music.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GenresService {

    private final GenresRepository genresRepository;

    public Map<Object, Object> verifyGenres(GenresDto genresDTO) {
        Map<Object, Object> finalResult = new HashMap<>();
        Result result = Result.OK();
        Boolean check = true;
        try {
            if (genresDTO.getCode().isEmpty() || genresRepository.checkCodeOrNameExists("code", genresDTO.getCode()) == 1) {
                result = new Result(Message.INVALID_MUSIC_GENRE_CODE.getCode(), false, Message.INVALID_MUSIC_GENRE_CODE.getMessage());
                check = false;
            }

            if (genresDTO.getName().isEmpty() || genresRepository.checkCodeOrNameExists("name", genresDTO.getName()) == 1) {
                result = new Result(Message.INVALID_MUSIC_GENRES_NAME.getCode(), false, Message.INVALID_MUSIC_GENRES_NAME.getMessage());
                check = false;
            }
            if (check) {
                finalResult.put(Constant.RESPONSE_KEY.DATA, genresDTO);
            } else {
                finalResult.put(Constant.RESPONSE_KEY.DATA, new Genres());
            }
        } catch (Exception e) {
            System.out.println("Xảy ra lỗi khi thực hiện xác minh thông tin ! {} " + e.getMessage());
            result = new Result(Message.UNABLE_TO_VERIFY_INFORMATION.getCode(), false, Message.UNABLE_TO_VERIFY_INFORMATION.getMessage());
        }
        finalResult.put(Constant.RESPONSE_KEY.RESULT, result);
        return finalResult;
    }

    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public Map<Object, Object> saveGenres(GenresDto genresDTO) {
        Map<Object, Object> finalResult = new HashMap<>();
        Result result = Result.OK();
        try {
            Genres genres = Genres.builder()
                    .id(UUID.randomUUID().toString())
                    .code(genresDTO.getCode())
                    .name(genresDTO.getName())
                    .status(Constant.Status.Activate)
                    .create_date(new Date(new java.util.Date().getTime()))
                    .create_by("SUBLIME_SYSTEM")
                    .build();
            this.genresRepository.save(genres);
            finalResult.put(Constant.RESPONSE_KEY.DATA, genres);
        } catch (Exception e) {
            System.out.println("Lỗi khi thực hiện thêm mới thể loại nhạc! {} " + e);
            result = new Result(Message.CANNOT_CREATE_NEW_GENRES.getCode(), false, Message.CANNOT_CREATE_NEW_GENRES.getMessage());
            throw e;
        }
        finalResult.put(Constant.RESPONSE_KEY.RESULT, result);
        return finalResult;
    }

    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public Map<Object, Object> updateGenres(String id, GenresDto genresDTO) {
        Map<Object, Object> finalResult = new HashMap<>();
        Result result = Result.OK();
        try {
            Genres genres = this.genresRepository.findById(id).orElse(null);
            if (genres == null) {
                result = new Result(Message.GENRES_DOES_NOT_EXIST.getCode(), false, Message.GENRES_DOES_NOT_EXIST.getMessage());
                finalResult.put(Constant.RESPONSE_KEY.RESULT, result);
                finalResult.put(Constant.RESPONSE_KEY.DATA, new Genres());
                return finalResult;
            } else {
                genres.setCode(genresDTO.getCode());
                genres.setName(genresDTO.getName());
                genres.setUpdate_date(new Date(new java.util.Date().getTime()));
                genres.setUpdate_by("SUBLIME_SYSTEM");
                this.genresRepository.save(genres);
                finalResult.put(Constant.RESPONSE_KEY.DATA, genres);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi thực hiện cập nhật thể loại nhạc! {} " + e.getMessage());
            result = new Result(Message.CANNOT_UPDATE_GENRES.getCode(), false, Message.CANNOT_UPDATE_GENRES.getMessage());
            throw e;
        }
        finalResult.put(Constant.RESPONSE_KEY.RESULT, result);
        return finalResult;
    }

    public Map<Object, Object> searchGenre(String id) {
        Map<Object, Object> finalResult = new HashMap<>();
        Result result = Result.OK();
        try {
            Genres genres = this.genresRepository.findById(id).orElse(null);
            if (genres == null) {
                result = new Result(Message.GENRES_DOES_NOT_EXIST.getCode(), false, Message.GENRES_DOES_NOT_EXIST.getMessage());
                finalResult.put(Constant.RESPONSE_KEY.RESULT, result);
                finalResult.put(Constant.RESPONSE_KEY.DATA, new Genres());
                return finalResult;
            } else {
                finalResult.put(Constant.RESPONSE_KEY.DATA, genres);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi thực hiện tìm kiếm thể loại nhạc! {} " + e.getMessage());
            result = new Result(Message.ERROR_WHILE_SEARCHING_FOR_GENRES.getCode(), false, Message.ERROR_WHILE_SEARCHING_FOR_GENRES.getMessage());
        }
        finalResult.put(Constant.RESPONSE_KEY.RESULT, result);
        return finalResult;
    }

    public Map<Object, Object> getAll(Pageable pageable) {
        Map<Object, Object> finalResult = new HashMap<>();
        Result result = Result.OK();
        try {
            Page<Genres> genres = this.genresRepository.getAll(pageable);
            if (genres.isEmpty()) {
                result = new Result(Message.LIST_IS_EMPTY.getCode(), false, Message.LIST_IS_EMPTY.getMessage());
                finalResult.put(Constant.RESPONSE_KEY.DATA, null);
                return finalResult;
            } else {
                finalResult.put(Constant.RESPONSE_KEY.DATA, genres);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi thực hiện lấy ra danh sách thể loại nhạc! {} " + e.getMessage());
            result = new Result(Message.ERROR_WHILE_RETRIEVING_GENRES_LIST.getCode(), false, Message.ERROR_WHILE_SEARCHING_FOR_SONGS.getMessage());
        }
        finalResult.put(Constant.RESPONSE_KEY.RESULT, result);
        return finalResult;
    }

    public Map<Object, Object> changeStatus(String id, String status) {
        Map<Object, Object> finalResult = new HashMap<>();
        Result result = Result.OK();
        try {
            Genres genres = genresRepository.findById(id).orElse(null);
            if (genres == null) {
                result = new Result(Message.GENRES_DOES_NOT_EXIST.getCode(), false, Message.GENRES_DOES_NOT_EXIST.getMessage());
                finalResult.put(Constant.RESPONSE_KEY.RESULT, result);
                finalResult.put(Constant.RESPONSE_KEY.DATA, new Genres());
            } else {
                genres.setStatus(status);
                genresRepository.save(genres);
                finalResult.put(Constant.RESPONSE_KEY.DATA, genres);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi thực hiện thay đổi trạng thái thể loại nhạc! {} " + e.getMessage());
            result = new Result(Message.CANNOT_UPDATE_STATUS.getCode(), false, Message.CANNOT_UPDATE_STATUS.getMessage());
        }
        finalResult.put(Constant.RESPONSE_KEY.RESULT, result);
        return finalResult;
    }

    public Map<Object, Object> getGenresForSelect() {
        Map<Object, Object> finalResult = new HashMap<>();
        Result result = Result.OK();
        try {
            List<ComboboxValue> list = this.genresRepository.getGenresForSelect();
            if (list.isEmpty()) {
                finalResult.put(Constant.RESPONSE_KEY.DATA, new ArrayList<>());
            } else {
                finalResult.put(Constant.RESPONSE_KEY.DATA, list);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi thực hiện lấy ra danh sách thể loại nhạc! {} " + e.getMessage());
            result = new Result(Message.ERROR_WHILE_RETRIEVING_GENRES_LIST.getCode(), false, Message.ERROR_WHILE_SEARCHING_FOR_SONGS.getMessage());
        }
        finalResult.put(Constant.RESPONSE_KEY.RESULT, result);
        return finalResult;
    }

}
