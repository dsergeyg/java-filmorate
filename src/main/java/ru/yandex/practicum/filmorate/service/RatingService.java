package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.List;

@Service
@Slf4j
public class RatingService {

    private final RatingStorage ratingStorage;

    @Autowired
    public RatingService(RatingStorage ratingStorage) {
        this.ratingStorage = ratingStorage;
    }

    public List<Rating> getRatings() {
        return ratingStorage.getRatings();
    }

    public Rating getRating(long id) {
        return ratingStorage.getRatingById(id);
    }

}
