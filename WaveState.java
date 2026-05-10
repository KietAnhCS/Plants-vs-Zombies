/**
 * Quản lý các trạng thái logic của Wave trong dự án Golden Spatula.
 * Luồng vận hành: 
 * Xong Wave -> Đợi 3s -> Nhận Sun -> Đợi 3s -> Dave nói -> Đợi 5s -> Augment -> Đợi 3s -> Wave mới.
 */
public enum WaveState 
{
    /** Đang trong trận đấu, Zombie đang tràn ra hoặc đang bị tiêu diệt */
    BATTLE,

    /** Zombie đã hết, đang đợi 3 giây để cấp Sun thưởng Wave */
    WAITING_FOR_REWARD,

    /** Đang đợi 3 giây sau khi nhận Sun để Dave xuất hiện */
    WAITING_FOR_DAVE,

    /** Dave đang xuất hiện và nói chuyện (Game tạm dừng các logic khác) */
    DAVE_TALKING,

    /** Đang đợi 5 giây sau khi Dave biến mất để hiện bảng chọn Augment */
    WAITING_FOR_AUGMENT,

    /** Người chơi đang thực hiện chọn 1 trong 3 thẻ nâng cấp Augment */
    SELECTING_AUGMENT,

    /** Đã chọn xong Augment, đợi 3 giây "bình yên" cuối cùng trước khi Wave mới bắt đầu */
    PREPARING_NEXT_WAVE;
}