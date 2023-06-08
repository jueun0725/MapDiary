package com.example.kakaomaptest.controller;

import com.example.kakaomaptest.model.Diary;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/diary")
public class DiaryController { //홈 화면에서 보여지는 캘린더
    private Map<Integer, Diary> diaryMap = new HashMap<>(); // 임시 데이터 저장용 맵
    private JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;

    @GetMapping
    public String showDiary() {
        // 캘린더 화면을 보여주는 로직
        return "diary";
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

            ObjectNode response = jsonNodeFactory.objectNode();
            response.put("code", 200);
            response.put("message", "장소 등록에 성공했습니다");
            return ResponseEntity.ok(response);

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
}
