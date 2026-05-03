package skladinya.application.converters.v1.booking;

import skladinya.application.dtos.v1.booking.BookingReceiptDto;
import skladinya.domain.models.booking.BookingReceipt;

public final class BookingReceiptDtoConverter {

    private BookingReceiptDtoConverter() {
    }

    public static BookingReceiptDto toDto(BookingReceipt bookingReceipt) {
        return BookingReceiptDto.builder()
                .booking(BookingGetUserDtoConverter.toDto(bookingReceipt.booking()))
                .receipt(bookingReceipt.receipt())
                .build();
    }
    
}
