package com.collabit.portfolio.controller;


import com.collabit.global.security.SecurityUtil;
import com.collabit.portfolio.domain.dto.getMultipleHexagonProgressResponseDTO;
import com.collabit.portfolio.domain.dto.getProgressResponseDTO;
import com.collabit.portfolio.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
@Tag(name = "PortfolioController", description = "Portfolio 생성 관련 API입니다.")
public class PortfolioController {
    private final PortfolioService portfolioService;

    @Operation(summary = "progress bar 데이터를 조회하는 API입니다."
            , description = "상대적 위치백분율값 조회(progressbar)")
    @GetMapping("/multiple/progressbar")
    public ResponseEntity<getMultipleHexagonProgressResponseDTO> getHexagonAndProgressbarGraph() {
        String userCode = SecurityUtil.getCurrentUserCode();

        return ResponseEntity.ok()
                .body(portfolioService.getHexagonAndProgressbarGraph(userCode);
    }
}

