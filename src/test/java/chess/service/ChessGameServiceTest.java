package chess.service;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dao.BoardDao;
import chess.dao.MockBoardDao;
import chess.dao.MockPieceDao;
import chess.dao.PieceDao;
import chess.domain.Color;
import chess.domain.board.Board;
import chess.domain.board.Column;
import chess.domain.board.Position;
import chess.domain.board.Row;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.dto.ChessBoardDto;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChessGameServiceTest {

    @Test
    @DisplayName("게임 시작 시 저장된 체스가 있으면 불러온다.")
    void initial_start() {
        //given
        Board board = new Board();
        PieceDao pieceDao = new MockPieceDao();
        BoardDao boardDao = new MockBoardDao();
        board.move(Position.of(Column.A, Row.TWO), Position.of(Column.A, Row.FOUR));
        pieceDao.save(board.getPiecesByPosition());
        boardDao.updateTurn(Color.BLACK,1);
        ChessGameService chessGameService = new ChessGameService(pieceDao, boardDao);
        //when
        chessGameService.start(1);
        //then
        ChessBoardDto chessBoardDto = chessGameService.getBoard();
        Map<String, Piece> pieceMap = chessBoardDto.getBoard();
        assertThat(pieceMap.get("a4")).isInstanceOf(Pawn.class);
    }

    @Test
    @DisplayName("게임을 종료하면 저장된 게임이 삭제된다.")
    void delete() {
        //given
        Board board = new Board();
        PieceDao pieceDao = new MockPieceDao();
        BoardDao boardDao = new MockBoardDao();
        pieceDao.save(board.getPiecesByPosition());
        boardDao.updateTurn(Color.WHITE, 1);
        ChessGameService chessGameService = new ChessGameService(pieceDao, boardDao);
        chessGameService.start(1);
        //when
        chessGameService.end(1);
        //then
        assertThat(pieceDao.existPieces()).isFalse();
    }
}
