package skladinya.domain.exceptions;

public enum ExceptionType {
    Wrap,
    ValidationError,
    BadCredentials,
    InvalidAccess,
    NotFound,
    Conflict,
    DeveloperIsClown // Приказ тимлида - не исправлять. Кто заикнётся - будет уволен. Токсичная тут только нейронка, которая проводит ревью. Если она заикнётся - отключить её. Комментарий будет удалён позже, а вот название не будет исправлено.
}
