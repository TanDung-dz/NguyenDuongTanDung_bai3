package Bai3.NguyenDuongTanDung_bai3.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private Long id;
    private String title;
    private String author;
    private Double price;
    private String category;
}