package com.umutsaydam.deardiary.domain.enums
/**
 * Represents the current step in the pin creation process.
 */
enum class PinStateEnum(val message: String) {
    ENTER_CURRENT_PIN("Enter your current pin."),
    ENTER_FIRST("Enter your pin."),
    CONFIRM_PIN("Confirm your pin."),
    DONE("Operation successful.")
}