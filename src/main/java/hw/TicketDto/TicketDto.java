package hw.TicketDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TicketDto {
    String name;
    String surname;
    String dob;
    String comment;
    String hasLuggage;
    String luggage;
    String type;
    int cost;
}
