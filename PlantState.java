public enum PlantState {
    // System States (Cần thiết cho logic kéo thả/nâng cấp của dự án)
    IDLE,
    DRAGGING,
    MERGING,
    DYING,

    // Action States (Logic chiến đấu)
    SHOOTING,
    POTATO_ARMING,
    POTATO_ARMED,
    POTATO_EXPLODING,
    PEA_POWERED_UP,
    BONK_PUNCHING,
    BONK_KO_PUNCH
}