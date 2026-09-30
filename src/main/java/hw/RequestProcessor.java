package hw;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import hw.ApiErrors.ApiErrors;
import hw.GenerateFile.GenerateFile;
import hw.TicketDto.TicketDto;

public class RequestProcessor {

    private static final SpamCheckerService SPAM_CHECKER = new SpamCheckerService(
            "sk-8_GTxy6qjliJFHIDVkDD3Zvh8pAViptL");

    private static final double MAX_SPAM_PROBABILITY = 0.1;

    public byte[] processRequest(String name, String surname, String dob, String comment,
            String hasLuggage, String luggage, String type, String fileType) {

        checkSpam(comment);

        if (!validateTicketData(type, dob, luggage)) {
            throw ApiErrors.badRequest("Невалидные данные");
        }

        int costTicket = calculateCostTicket(type, luggage);

        TicketDto ticketDto = new TicketDto(name, surname, dob, comment, hasLuggage, luggage, type, costTicket);

        if (fileType.equalsIgnoreCase("PDF")) {
            return GenerateFile.generatePdf(ticketDto);
        }

        if (fileType.equalsIgnoreCase("PNG")) {
            return GenerateFile.generatePng(ticketDto);
        }

        return null;
    }

    private boolean validateTicketData(String type, String dob, String luggage) {

        LocalDate birthDate = LocalDate.parse(dob,
                DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        int age = Period.between(birthDate, LocalDate.now()).getYears();

        int intLuggage = Integer.parseInt(luggage);

        return switch (type) {
            case "AVIA" -> age > 17 && intLuggage <= 5;
            case "RAILWAY" -> age > 20 && intLuggage <= 3;
            default -> false;
        };
    }

    private int calculateCostTicket(String type, String luggage) {

        int intLuggage = Integer.parseInt(luggage);

        switch (type) {
            case "AVIA":
                int costAviaTicket = 1000;
                return (intLuggage * 500) + costAviaTicket;

            case "RAILWAY":
                int costRailwayTicket = 750;
                return intLuggage > 2 ? ((intLuggage - 2) * 300) + costRailwayTicket : costRailwayTicket;
            default:
                return 0;
        }
    }

    private void checkSpam(String text) {
        if (text == null || text.isBlank()) {
            return;
        }
        try {
            SpamCheckerService.SpamCheckResults result = SPAM_CHECKER.verifySpam(text);
            if (result.getSpamProbability() >= MAX_SPAM_PROBABILITY) {
                throw ApiErrors.badRequest(
                        "Комментарий отклонён: " + result.getShortSpamDescription());
            }
        } catch (Exception e) {
            throw ApiErrors.badRequest("Не удалось проверить комментарий на спам");
        }
    }
}
