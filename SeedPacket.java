import greenfoot.*;

public class SeedPacket extends Actor
{
    public long deltaTime;
    public long rechargeTime; 
    public long lastFrame; 
    
    public int sunCost;
    public String name;
    
    public boolean recharged = false; 
    public boolean selected = false;  
    
    private GreenfootImage imageBright; 
    private GreenfootImage imageDark;   
    private GreenfootImage rechargeOverlay; 
    
    private MyWorld myWorld;
    private boolean doneRechargeTime = false;

    public SeedPacket(long rechargeTime, boolean recharged, int sunCost, String name) {
        this.rechargeTime = rechargeTime;
        this.recharged = recharged;
        this.sunCost = sunCost;
        this.name = name;
        
        this.imageBright = new GreenfootImage(name + "1.png"); 
        this.imageDark = new GreenfootImage(name + "2.png");   
        
        this.lastFrame = System.currentTimeMillis();
        setImage(imageDark);
    }

    public void addedToWorld(World world) {
        myWorld = (MyWorld)world;
        rechargeOverlay = new GreenfootImage(imageBright.getWidth(), imageBright.getHeight());
        if (recharged) {
            doneRechargeTime = true;
            updateAppearance();
        }
    }

    public void act() {
        updateCooldown();
        updateAppearance();
    }

    private void updateCooldown() {
        if (!doneRechargeTime) {
            deltaTime = System.currentTimeMillis() - lastFrame;
            
            if (deltaTime >= rechargeTime) {
                doneRechargeTime = true;
                recharged = true;
            }
        }
    }

    public void updateAppearance() {
        if (myWorld == null || myWorld.seedbank == null) return;

        // Nếu đang hồi chiêu
        if (!doneRechargeTime) {
            GreenfootImage tempImage = new GreenfootImage(imageDark);
            rechargeOverlay.clear();
            rechargeOverlay.setColor(new Color(0, 0, 0, 150)); 
            
            double progress = (double)deltaTime / rechargeTime;
            int height = (int)(tempImage.getHeight() * (1.0 - progress));
            
            rechargeOverlay.fillRect(0, 0, tempImage.getWidth(), height);
            tempImage.drawImage(rechargeOverlay, 0, 0);
            setImage(tempImage);
            recharged = false;
        } 
        // Nếu đã hồi xong
        else {
            // SỬA LỖI Ở ĐÂY: Đổi suncounter thành sunCounter cho khớp với lớp SeedBank
            int currentSun = myWorld.seedbank.sunCounter.sun; 

            if (currentSun < sunCost || selected) {
                setImage(imageDark);
                // Vẫn giữ trạng thái recharged = true nếu đủ tiền để SeedBank biết mà cho chọn
                recharged = (currentSun >= sunCost); 
            } else {
                setImage(imageBright);
                recharged = true;
            }
        }
    }

    public void startRecharge() {
        lastFrame = System.currentTimeMillis();
        doneRechargeTime = false;
        recharged = false;
    }

    public void setSelected(boolean bool) {
        this.selected = bool;
    }

    public TransparentObject addImage() {
        return null; 
    }

    public Plant getPlant() {
        // Hàm này sẽ được override ở các lớp con
        return null;
    }
}