package com.maple.maple.nexon.controller;

import com.maple.maple.nexon.dto.response.OcidListResponse;
import com.maple.maple.nexon.service.NexonService;
import com.maple.maple.response.response.ApiResponse;
import com.maple.maple.response.response.ApiResponseMapleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/maple")
public class NexonController {

    private final NexonService nexonService;

    @GetMapping("/team")
    public ResponseEntity<ApiResponse<OcidListResponse>> getTeamOcid(){
        OcidListResponse result = nexonService.getTeamOcid();
        return ResponseEntity.ok(ApiResponse.success(result, ApiResponseMapleEnum.TEAM_OCID_GET_SUCCESS.getMessage()));
    }
}
