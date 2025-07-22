package com.example.finalproject.controller;

import com.example.finalproject.dto.StyleDTO;
import com.example.finalproject.service.StyleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/styles")
@CrossOrigin(origins = "http://localhost:4200")
public class StyleController {

    private final StyleService styleService;

    public StyleController(StyleService styleService) {
        this.styleService = styleService;
    }


    @GetMapping
    public ResponseEntity<List<StyleDTO>> getAllStyles() {
        return ResponseEntity.ok(styleService.getAllStyles());
    }


    @GetMapping("/{id}")
    public ResponseEntity<StyleDTO> getStyleById(@PathVariable Long id) {
        Optional<StyleDTO> style = styleService.getStyleById(id);
        return style.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<StyleDTO> createStyle(@RequestBody StyleDTO styleDTO) {
        Optional<StyleDTO> createdStyle = styleService.createStyle(styleDTO);
        return createdStyle
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<StyleDTO> updateStyle(@PathVariable Long id, @RequestBody StyleDTO styleDTO) {
        Optional<StyleDTO> updatedStyle = styleService.updateStyle(id, styleDTO);
        return updatedStyle.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStyle(@PathVariable Long id) {
        boolean isDeleted = styleService.deleteStyle(id);
        return isDeleted
                ? ResponseEntity.ok("✅Style has been deleted!")
                : ResponseEntity.notFound().build();
    }
}
