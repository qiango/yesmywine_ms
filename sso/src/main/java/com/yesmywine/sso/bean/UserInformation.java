package com.yesmywine.sso.bean;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by SJQ on 2017/6/18.
 */

@Entity
@Table(name = "userInformation")
public class UserInformation extends BaseEntity<Integer>{
    @Column(unique = true,columnDefinition = "varchar(50) COMMENT '用户名'")
    private  String userName;
    @Column(unique = true,columnDefinition = "varchar(50) COMMENT '登录密码'")
    private String password;//登录密码
    @Column(unique = true,columnDefinition = "varchar(50) COMMENT '支付密码'")
    private String paymentPassword;//支付密码
    @Column(unique = true,columnDefinition = "varchar(50) COMMENT '手机号'")
    private String phoneNumber;
    @Column(columnDefinition = "BIT(1) COMMENT '是否绑定手机'")
    private Boolean bindPhoneFlag;//是否绑定手机号
    @Column(columnDefinition = "varchar(50) COMMENT '用户名'")
    private String nickName;
    @Column(unique = true,columnDefinition = "varchar(100) COMMENT '邮箱'")
    private String email;
    @Column(columnDefinition = "BIT(1) COMMENT '是否绑定邮箱（T：是， F：否）'")
    private Boolean bindEmailFlag;//是否绑定邮箱（T：是， F：否）
    @Column(columnDefinition = "DATETIME COMMENT '邮箱激活时间'")
    private Date activationTime;//邮箱激活时间
    @Column(columnDefinition = "varchar(50) COMMENT '随机码'")
    private String codeEmail;//随机码
    @Column(columnDefinition = "varchar(100) COMMENT '身份证号码'")
    private String IDCardNum;//身份证号码
    @Column(columnDefinition = "double COMMENT '酒豆'")
    private Double bean;
    @Column(columnDefinition = "varchar(50) COMMENT '注册渠道'")
    private String registerChannel;//注册渠道
    @Column(columnDefinition = "tinyint default 0")
    private Integer growthValue;//成长值
    @Column(columnDefinition = "double COMMENT '余额'")
    private Double remainingSum;//余额
    @Column(columnDefinition = "varchar(100) COMMENT '自发升降级时间'")
    private String voluntarily;//自发升降级时间
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "levelId")
    private VipRule vipRule;//会员等级
    @Column(columnDefinition = "int(1) COMMENT '0-需要同步,1-不需要'")
    private Integer synStatus;//同步状况 0-需要同步
    @Column(columnDefinition = "int(5) COMMENT '渠道分類(0-門店 ，1-官網 )'")
    private Integer channelType;//渠道分類(0-門店 ，1-官網 )
    @Column(columnDefinition = "varchar(100) COMMENT '用户图片'")
    private String userImg;
    @Column(columnDefinition = "varchar(50) COMMENT '真名'")
    private String realName;
    @Column(columnDefinition = "varchar(10) COMMENT '性别'")
    private String sex;
    @Column(columnDefinition = "varchar(50) COMMENT '生日'")
    private String birthday;
    @Column(columnDefinition = "varchar(50) COMMENT '固定电话'")
    private String fixedPhone;


    public String getFixedPhone() {
        return fixedPhone;
    }

    public void setFixedPhone(String fixedPhone) {
        this.fixedPhone = fixedPhone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCodeEmail() {
        return codeEmail;
    }

    public void setCodeEmail(String codeEmail) {
        this.codeEmail = codeEmail;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public UserInformation() {
        this.bean=Double.valueOf(0);
        this.growthValue = 0;
        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.YEAR,curr.get(Calendar.YEAR)+1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.voluntarily=sdf.format(curr.getTime());
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIDCardNum() {
        return IDCardNum;
    }

    public void setIDCardNum(String IDCardNum) {
        this.IDCardNum = IDCardNum;
    }

    public Double getBean() {
        return bean;
    }

    public void setBean(Double bean) {
        this.bean = bean;
    }

    public String getVoluntarily() {
        return voluntarily;
    }

    public void setVoluntarily(String voluntarily) {
        this.voluntarily = voluntarily;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public VipRule getVipRule() {
        return vipRule;
    }

    public void setVipRule(VipRule vipRule) {
        this.vipRule = vipRule;
    }

    public Integer getGrowthValue() {
        return growthValue;
    }

    public void setGrowthValue(Integer growthValue) {
        this.growthValue = growthValue;
    }

    public String getRegisterChannel() {
        return registerChannel;
    }

    public void setRegisterChannel(String registerChannel) {
        this.registerChannel = registerChannel;
    }

    public Double getRemainingSum() {
        return remainingSum;
    }

    public void setRemainingSum(Double remainingSum) {
        this.remainingSum = remainingSum;
    }

    public Boolean getBindPhoneFlag() {
        return bindPhoneFlag;
    }

    public void setBindPhoneFlag(Boolean bindPhoneFlag) {
        this.bindPhoneFlag = bindPhoneFlag;
    }

    public Boolean getBindEmailFlag() {
        return bindEmailFlag;
    }

    public void  setBindEmailFlag(Boolean bindEmailFlag) {
        this.bindEmailFlag = bindEmailFlag;
    }

    public Integer getSynStatus() {
        return synStatus;
    }

    public void setSynStatus(Integer synStatus) {
        this.synStatus = synStatus;
    }

    public Integer getChannelType() {
        return channelType;
    }

    public void setChannelType(Integer channelType) {
        this.channelType = channelType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPaymentPassword() {
        return paymentPassword;
    }

    public void setPaymentPassword(String paymentPassword) {
        this.paymentPassword = paymentPassword;
    }

    public Date getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(Date activationTime) {
        this.activationTime = activationTime;
    }

}
