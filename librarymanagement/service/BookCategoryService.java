package librarymanagement.service;

import librarymanagement.model.BookCategory;
import librarymanagement.repository.BookCategoryRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BookCategoryService {
    private final BookCategoryRepository categoryRepository;

    public BookCategoryService(BookCategoryRepository categoryRepository) {
        this.categoryRepository = Objects.requireNonNull(
                categoryRepository, "categoryRepository must not be null");
    }

    public Optional<BookCategory> findByCategoryId(String categoryId) throws SQLException {
        return categoryRepository.findByCategoryId(categoryId);
    }

    public List<BookCategory> findAll() throws SQLException {
        return categoryRepository.findAll();
    }

    public void create(BookCategory category) throws SQLException {
        categoryRepository.save(
                Objects.requireNonNull(category, "category must not be null"));
    }

    public boolean update(BookCategory category) throws SQLException {
        return categoryRepository.update(
                Objects.requireNonNull(category, "category must not be null"));
    }

    public boolean deleteByCategoryId(String categoryId) throws SQLException {
        return categoryRepository.deleteByCategoryId(categoryId);
    }
}
