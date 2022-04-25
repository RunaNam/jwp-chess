package chess.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import chess.domain.Winner;
import chess.domain.board.Board;
import chess.dto.ChessBoardDto;
import chess.dto.ResponseDto;
import chess.dto.ResultDto;
import chess.dto.StatusDto;
import chess.service.ChessGameService;
import chess.service.ResponseCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChessSpringController.class)
public class ChessSpringControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChessGameService chessGameService;

    @DisplayName("start GET 요청 테스트")
    @Test
    void get_start() throws Exception {
        mockMvc.perform(get("/start"))
                .andExpect(status().isOk());
    }

    @DisplayName("chess GET 요청 테스트")
    @Test
    void get_chess() throws Exception {
        final ChessBoardDto chessBoardDto = ChessBoardDto.from(new Board().getPiecesByPosition());
        given(chessGameService.getBoard()).willReturn(ChessBoardDto.from(new Board().getPiecesByPosition()));

        mockMvc.perform(get("/chess"))
                .andExpect(view().name("index"))
                .andExpect(model().attribute("boardDto", chessBoardDto));
    }

    @DisplayName("move POST 요청 테스트")
    @Test
    void post_move() throws Exception {
        final String requestString = "a2 a4";
        final ResponseDto responseDto = new ResponseDto(302, "");
        given(chessGameService.move("a2", "a4")).willReturn(ResponseCode.FOUND);
        mockMvc.perform(post("/move")
                        .content(requestString))
                .andExpect(status().isFound());
    }

    @DisplayName("status GET 요청 테스트")
    @Test
    void get_status() throws Exception {
        final StatusDto statusDto = StatusDto.of(37, 37);
        given(chessGameService.statusOfBlack()).willReturn(37.0);
        given(chessGameService.statusOfWhite()).willReturn(37.0);

        mockMvc.perform(get("/chess-status"))
                .andExpect(view().name("status"))
                .andExpect(model().attribute("status", statusDto));
    }

    @DisplayName("end GET 요청 테스트")
    @Test
    void get_end() throws Exception {
        mockMvc.perform(get("/end"))
                .andExpect(status().isOk());
    }

    @DisplayName("result GET 요청 테스트")
    @Test
    void get_result() throws Exception {
        final ResultDto resultDto = ResultDto.of(36, 37, Winner.BLACK);

        given(chessGameService.statusOfWhite()).willReturn(36.0);
        given(chessGameService.statusOfBlack()).willReturn(37.0);
        given(chessGameService.findWinner()).willReturn(Winner.BLACK);

        mockMvc.perform(get("/chess-result"))
                .andExpect(view().name("result"))
                .andExpect(model().attribute("result", resultDto));
    }
}
