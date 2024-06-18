package com.bookshop.catalogservice.domain;

import com.bookshop.catalogservice.domain.Book.OnCreate;
import com.bookshop.catalogservice.domain.Book.OnUpdate;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public record Measure(
        @NotNull(groups = OnCreate.class, message = "The width of book must not be null")
        @Min(groups = {OnCreate.class, OnUpdate.class}, value = 40, message = "The width of book must greater than 4 cm")
        @Max(groups = {OnCreate.class, OnUpdate.class}, value = 300, message = "The width of book must less than 30 cm")
        double width,
        @NotNull(groups = OnCreate.class, message = "The height of book must not be null")
        @Min(groups = {OnCreate.class, OnUpdate.class}, value = 60, message = "The height of book must greater than 6 cm")
        @Max(groups = {OnCreate.class, OnUpdate.class}, value = 400, message = "The height of book must less than 40 cm")
        double height,
        @NotNull(groups = OnCreate.class, message = "The thickness of book must not be null")
        @Min(groups = {OnCreate.class, OnUpdate.class}, value = 1, message = "The thickness of book must greater than 0.1 cm")
        @Max(groups = {OnCreate.class, OnUpdate.class}, value = 100, message = "The thickness of book must less than 10 cm")
        double thickness,
        @NotNull(groups = OnCreate.class, message = "The weight of book must not be null")
        @Min(groups = {OnCreate.class, OnUpdate.class}, value = 170, message = "The lowest weight of book is 170 grams. ")
        double weight // grams
) {
}
