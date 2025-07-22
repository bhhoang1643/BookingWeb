package com.example.finalproject.controller;

import com.example.finalproject.dto.StyleTagDTO;
import com.example.finalproject.service.StyleTagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/style-tags")
@CrossOrigin(origins = "http://localhost:4200")
public class StyleTagController {

    private final StyleTagService styleTagService;

    public StyleTagController(StyleTagService styleTagService) {
        this.styleTagService = styleTagService;
    }

    @GetMapping
    public ResponseEntity<List<StyleTagDTO>> getAllTags() {
        return ResponseEntity.ok(styleTagService.getAllTags());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StyleTagDTO> getTagById(@PathVariable Long id) {
        return ResponseEntity.ok(styleTagService.getTagById(id));
    }

    @PostMapping
    public ResponseEntity<StyleTagDTO> createTag(@RequestBody StyleTagDTO dto) {
        return ResponseEntity.status(201).body(styleTagService.createTag(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StyleTagDTO> updateTag(@PathVariable Long id, @RequestBody StyleTagDTO dto) {
        return ResponseEntity.ok(styleTagService.updateTag(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        styleTagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
