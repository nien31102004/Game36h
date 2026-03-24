package com.example.gamesearch.repository;

import com.example.gamesearch.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    // Tìm game theo tên (không phân biệt hoa thường)
    List<Game> findByNameContainingIgnoreCase(String name);

    // Tìm game theo thể loại
    List<Game> findByGenreIgnoreCase(String genre);

    // Tìm game theo tên VÀ thể loại
    List<Game> findByNameContainingIgnoreCaseAndGenreIgnoreCase(String name, String genre);

    // Lấy tất cả game sắp xếp theo lượt chơi giảm dần
    List<Game> findAllByOrderByPlayCountDesc();

    // Lấy danh sách các thể loại (không trùng lặp)
    @Query("SELECT DISTINCT g.genre FROM Game g ORDER BY g.genre")
    List<String> findAllGenres();

    // Tìm kiếm nâng cao với query tùy chỉnh
    @Query("SELECT g FROM Game g WHERE " +
           "(:name IS NULL OR LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:genre IS NULL OR LOWER(g.genre) = LOWER(:genre)) " +
           "ORDER BY g.playCount DESC")
    List<Game> searchGames(@Param("name") String name, @Param("genre") String genre);
}
