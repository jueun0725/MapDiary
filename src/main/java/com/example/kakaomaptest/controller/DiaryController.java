package com.example.kakaomaptest.controller;

import com.example.kakaomaptest.model.Diary;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/diary")
public class DiaryController { //홈 화면에서 보여지는 캘린더
    private Map<Integer, Diary> diaryMap = new HashMap<>(); // 임시 데이터 저장용 맵
    private JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;

    @GetMapping
    public ModelAndView showDiary() {
        ModelAndView modelAndView = new ModelAndView("diary");
        modelAndView.addObject("diaries", diaryMap.values());
        return modelAndView;
    }

    //장소 등록 API
    @PostMapping("/register")
    public ResponseEntity<?> registerDiary(@RequestParam("date") String date,
                                           @RequestParam("title") String title,
                                           @RequestParam("description") String description,
                                           @RequestParam("latitude") double latitude,
                                           @RequestParam("longitude") double longitude,
                                           @RequestParam("placeName") String placeName) {

        try {
            if (title == null || title.isEmpty() || description == null || description.isEmpty() || placeName == null || placeName.isEmpty()) {
                ObjectNode response = jsonNodeFactory.objectNode();
                response.put("code", 400);
                response.put("message", "모든 항목은 필수 입력 사항입니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Diary 객체를 생성하여 날짜, 제목, 장소, 설명을 저장
            Diary diary = Diary.createDiary(date, title, description, latitude, longitude, placeName);
            diaryMap.put(diary.getId(), diary);

            // 사용자를 location.html로 리다이렉트
            URI location = new URI("location.html");
            return ResponseEntity.status(HttpStatus.OK).location(location).build();

        } catch (Exception e) {
            ObjectNode response = jsonNodeFactory.objectNode();
            response.put("code", 500);
            response.put("message", "장소 등록에 실패했습니다");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 임시 맵에 저장된 데이터를 확인하는 API
    @GetMapping("/registers")
    public ResponseEntity<?> getDiaries() {
        try {
            if (diaryMap.isEmpty()) {
                ObjectNode response = jsonNodeFactory.objectNode();
                response.put("code", 204);
                response.put("message", "캘린더에 등록된 내용이 없습니다");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }
            return ResponseEntity.ok(diaryMap);

        } catch (Exception e) {
            ObjectNode response = jsonNodeFactory.objectNode();
            response.put("code", 500);
            response.put("message", "캘린더 내용 조회에 실패했습니다");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //장소 삭제 API
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDiary(@PathVariable("id") int id) {
        try {
            if (!diaryMap.containsKey(id)) {
                ObjectNode response = jsonNodeFactory.objectNode();
                response.put("code", 404);
                response.put("message", "해당하는 장소를 찾을 수 없습니다");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            diaryMap.remove(id);

            ObjectNode response = jsonNodeFactory.objectNode();
            response.put("code", 200);
            response.put("message", "장소 삭제에 성공했습니다");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ObjectNode response = jsonNodeFactory.objectNode();
            response.put("code", 500);
            response.put("message", "장소 삭제에 실패했습니다");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 이벤트 수정 API
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable("id") int id,
                                         @RequestParam(value = "title", required = false) String title,
                                         @RequestParam(value = "description", required = false) String description,
                                         @RequestParam(value = "latitude", required = false) Double latitude,
                                         @RequestParam(value = "longitude", required = false) Double longitude,
                                         @RequestParam(value = "placeName", required = false) String placeName) {
        try {
            if (!diaryMap.containsKey(id)) {
                ObjectNode response = jsonNodeFactory.objectNode();
                response.put("code", 404);
                response.put("message", "해당 이벤트를 찾을 수 없습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Diary diary = diaryMap.get(id);

            // 수정할 필드들만 업데이트
            if (title != null) {
                diary.setTitle(title);
            }
            if (description != null) {
                diary.setDescription(description);
            }
            if (latitude != null) {
                diary.setLatitude(latitude);
            }
            if (longitude != null) {
                diary.setLongitude(longitude);
            }
            if (placeName != null) {
                diary.setPlaceName(placeName);
            }

            // 수정된 이벤트 정보를 저장
            diaryMap.put(id, diary);

            ObjectNode response = jsonNodeFactory.objectNode();
            response.put("code", 200);
            response.put("message", "이벤트 수정에 성공했습니다.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            ObjectNode response = jsonNodeFactory.objectNode();
            response.put("code", 500);
            response.put("message", "이벤트 수정에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
