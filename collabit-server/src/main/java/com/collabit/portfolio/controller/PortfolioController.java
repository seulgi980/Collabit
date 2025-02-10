package com.collabit.portfolio.controller;


import com.collabit.global.security.SecurityUtil;
import com.collabit.portfolio.domain.dto.GetAverageResponseDTO;
import com.collabit.portfolio.domain.dto.GetMultipleHexagonProgressResponseDTO;
import com.collabit.portfolio.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
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

    @Operation(summary = "육각형과 상대위치 progress bar를 채우기 위한 데이터를 조회하는 API입니다."
        , description = "객관식 6개 영역별 1.평균값+피드백문구,  2.각 영역별 description 3.상대적 위치백분율값 조회(progressbar)")
    @GetMapping("/multiple/graph")
    public ResponseEntity<GetMultipleHexagonProgressResponseDTO> getHexagonAndProgressbarGraph() {
        String userCode = SecurityUtil.getCurrentUserCode();

        return ResponseEntity.ok()
                .body(portfolioService.getHexagonAndProgressbarGraph(userCode));
    }

    @PostMapping("/")
    public ResponseEntity<?> generate() {
        String userCode = SecurityUtil.getCurrentUserCode();
        portfolioService.generatePortfolio(userCode);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/portfolio/main")
    public ResponseEntity<?> getAverage() {
        GetAverageResponseDTO responseDTO = portfolioService.getAverage();
        return ResponseEntity.ok().body(responseDTO);
    }
}

