package my.edu.sunway.wbrms.wbrmsreservationservice.dto;

public record SearchRequest(
        ChronologicalOrder order,
        SortBy sortBy
) {
}
