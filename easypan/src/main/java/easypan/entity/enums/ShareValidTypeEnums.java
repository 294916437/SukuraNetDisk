package easypan.entity.enums;

public enum ShareValidTypeEnums {
    DAY_1(0,1,"1天"),
    DAY_7(1,7,"7天"),
    DAY_30(2,30,"30天"),
    FOREVER(3,-1,"永久有效");

    private Integer type;
    private Integer days;
    private String desc;
    ShareValidTypeEnums(Integer type,Integer days,  String desc) {
        this.days = days;
        this.type = type;
        this.desc = desc;
    }
    public static ShareValidTypeEnums getEnumByType(Integer type){
        for(ShareValidTypeEnums enums:ShareValidTypeEnums.values()){
            if(enums.getType().equals(type)){
                return enums;
            }
        }
        return null;
    }
    public Integer getType() {
        return type;
    }

    public Integer getDays() {
        return days;
    }

    public String getDesc() {
        return desc;
    }
}
