package com.example.finalproject.controller;

import com.example.finalproject.dto.HairstylistDTO;
import com.example.finalproject.service.HairstylistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.finalproject.config.JwtUtils;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/hairstylists")
@CrossOrigin(origins = "http://localhost:4200")
public class HairstylistController {

    private final HairstylistService hairstylistService;
    private final JwtUtils jwtUtils;

    public HairstylistController(HairstylistService hairstylistService, JwtUtils jwtUtils) {
        this.hairstylistService = hairstylistService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/my/shop/{shopId}")
    public ResponseEntity<?> getMyHairstylistsByShop(@PathVariable Integer shopId,
                                                     @RequestHeader("Authorization") String authHeader) {
        Integer accountId = jwtUtils.extractAccountId(authHeader.replace("Bearer ", ""));
        return ResponseEntity.ok(hairstylistService.getByShopAndAccount(shopId, accountId));
    }
    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<HairstylistDTO>> getByShop(@PathVariable Integer shopId) {
        return ResponseEntity.ok(hairstylistService.getByShopId(shopId));
    }



    @PostMapping
    public ResponseEntity<?> createHairstylist(@RequestParam("shopId") Integer shopId,
                                               @RequestParam("name") String name,
                                               @RequestParam("experience") Integer experience,
                                               @RequestParam("specialty") String specialty,
                                               @RequestParam(value = "image", required = false) MultipartFile image,
                                               @RequestHeader("Authorization") String authHeader) throws IOException {
        Integer accountId = jwtUtils.extractAccountId(authHeader.replace("Bearer ", ""));
        HairstylistDTO dto =
                new HairstylistDTO(null, shopId, name, experience, specialty, null);
        return ResponseEntity.status(201).body(hairstylistService.createHairstylistSecure(dto, image, accountId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHairstylist(@PathVariable Integer id,
                                               @RequestParam("name") String name,
                                               @RequestParam("experience") Integer experience,
                                               @RequestParam("specialty") String specialty,
                                               @RequestParam(value = "image", required = false) MultipartFile image,
                                               @RequestHeader("Authorization") String authHeader) throws IOException {
        Integer accountId = jwtUtils.extractAccountId(authHeader.replace("Bearer ", ""));
        HairstylistDTO dto = new HairstylistDTO(null, null, name, experience, specialty, null);
        return ResponseEntity.ok(hairstylistService.updateHairstylistSecure(id, dto, image, accountId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHairstylist(@PathVariable Integer id,
                                               @RequestHeader("Authorization") String authHeader) {
        Integer accountId = jwtUtils.extractAccountId(authHeader.replace("Bearer ", ""));
        hairstylistService.deleteHairstylistSecure(id, accountId);
        return ResponseEntity.ok("✅ Hairstylist has been deleted!");
    }
}
