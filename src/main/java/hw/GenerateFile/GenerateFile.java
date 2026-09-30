package hw.GenerateFile;

import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

import hw.ApiErrors.ApiErrors;
import hw.TicketDto.TicketDto;

import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;

import com.lowagie.text.Document;

public class GenerateFile {

    private static final com.lowagie.text.Font PDF_FONT = loadPdfFont();
    private static final java.awt.Font PNG_FONT = loadPngFont();

    private static com.lowagie.text.Font loadPdfFont() {
        try {
            BaseFont baseFont = BaseFont.createFont(
                    "fonts/DejaVuSans.ttf",
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED);
            return new com.lowagie.text.Font(baseFont, 12);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(
                    "Не удалось загрузить шрифт для PDF: " + e.getMessage());
        }
    }

    private static java.awt.Font loadPngFont() {
        try (InputStream is = GenerateFile.class.getClassLoader()
                .getResourceAsStream("fonts/DejaVuSans.ttf")) {
            if (is == null) {
                throw new IllegalStateException("fonts/DejaVuSans.ttf не найден");
            }
            return java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, is)
                    .deriveFont(20f);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(
                    "Не удалось загрузить шрифт для PNG: " + e.getMessage());
        }
    }

    public static byte[] generatePdf(TicketDto dto) {

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);

            document.open();

            document.add(new Paragraph("Билет", PDF_FONT));
            document.add(new Paragraph("Имя: " + dto.getName(), PDF_FONT));
            document.add(new Paragraph("Фамилия: " + dto.getSurname(), PDF_FONT));
            document.add(new Paragraph("Дата рождения: " + dto.getDob(), PDF_FONT));
            document.add(new Paragraph("Комментарий: " + dto.getComment(), PDF_FONT));
            document.add(new Paragraph("Багаж: " + ("1".equals(dto.getHasLuggage()) ? "Есть" : "Нет"), PDF_FONT));
            document.add(new Paragraph("Количество багажа: " + dto.getLuggage(), PDF_FONT));
            document.add(new Paragraph("Тип: " + dto.getType(), PDF_FONT));
            document.add(new Paragraph("Стоимость: " + dto.getCost(), PDF_FONT));

            document.close();

            return out.toByteArray();
        } catch (Exception e) {
            throw ApiErrors.badRequest("Ошибка генерации PDF");
        }
    }

    public static byte[] generatePng(TicketDto dto) {

        int width = 800;
        int height = 500;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        try {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);

            g.setColor(Color.BLACK);
            g.setFont(PNG_FONT);

            String[] lines = {
                    "Билет",
                    "Имя: " + dto.getName(),
                    "Фамилия: " + dto.getSurname(),
                    "Дата рождения: " + dto.getDob(),
                    "Комментарий: " + dto.getComment(),
                    "Багаж: " + ("1".equals(dto.getHasLuggage()) ? "Есть" : "Нет"),
                    "Количество багажа: " + dto.getLuggage(),
                    "Тип: " + dto.getType(),
                    "Стоимость: " + dto.getCost()
            };

            int y = 50;
            for (String line : lines) {
                g.drawString(line, 40, y);
                y += 35;
            }
        } finally {
            g.dispose();
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", out);
            return out.toByteArray();
        } catch (IOException e) {
            throw ApiErrors.badRequest("Ошибка генерации png");
        }
    }
}
