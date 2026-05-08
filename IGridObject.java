/**
 * <<interface>>
 * IGridObject: Định nghĩa các đối tượng nằm trong hệ thống lưới của game.
 * Giúp quản lý vị trí theo hàng (row) để xử lý va chạm và logic tấn công.
 */
public interface IGridObject 
{
    /**
     * + getYPos() : int
     * Trả về chỉ số hàng (lane/row) mà đối tượng đang đứng.
     * Ví dụ: hàng 0, 1, 2, 3, 4.
     * 
     * @return Số nguyên đại diện cho hàng hiện tại.
     */
    int getYPos();
}