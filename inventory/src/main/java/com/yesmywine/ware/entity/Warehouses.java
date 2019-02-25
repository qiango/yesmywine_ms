package com.yesmywine.ware.entity;

import javax.persistence.*;

/**
 * Created by SJQ on 2017/1/5.
 *
 * @Description:仓库表
 */
@Entity
@Table(name = "warehouse")
public class Warehouses extends BaseEntity {
    @Id
    private Integer id;
    private String warehouseCode;
    private String warehouseName;
    private String warehouseProvince;
    private String warehouseCity;
    private String warehouseRegion;
    private String warehouseProvinceId;
    private String warehouseCityId;
    private String warehouseRegionId;
    private String warehouseAddress;
    private Integer type;  //类别  0-门店仓、1-实体仓、2-未清关仓库、3-已清关仓
    private String contactName;//联系人名称
    private String telephone;
    private String phone;
    private String fax;
    private String email;
    private String comment;
    private String status;
    @Column(columnDefinition = "BIT(1) COMMENT '是否能删除'")
    private Boolean canDelete;
    @Column(columnDefinition = "varchar(50) COMMENT '已清关与未清关关联编码'")
    private String relationCode;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getWarehouseProvince() {
        return warehouseProvince;
    }

    public void setWarehouseProvince(String warehouseProvince) {
        this.warehouseProvince = warehouseProvince;
    }

    public String getWarehouseCity() {
        return warehouseCity;
    }

    public void setWarehouseCity(String warehouseCity) {
        this.warehouseCity = warehouseCity;
    }

    public String getWarehouseAddress() {
        return warehouseAddress;
    }

    public void setWarehouseAddress(String warehouseAddress) {
        this.warehouseAddress = warehouseAddress;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getWarehouseRegion() {
        return warehouseRegion;
    }

    public void setWarehouseRegion(String warehouseRegion) {
        this.warehouseRegion = warehouseRegion;
    }

    public String getWarehouseProvinceId() {
        return warehouseProvinceId;
    }

    public void setWarehouseProvinceId(String warehouseProvinceId) {
        this.warehouseProvinceId = warehouseProvinceId;
    }

    public String getWarehouseCityId() {
        return warehouseCityId;
    }

    public void setWarehouseCityId(String warehouseCityId) {
        this.warehouseCityId = warehouseCityId;
    }

    public String getWarehouseRegionId() {
        return warehouseRegionId;
    }

    public void setWarehouseRegionId(String warehouseRegionId) {
        this.warehouseRegionId = warehouseRegionId;
    }

    public Boolean getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }

    public String getRelationCode() {
        return relationCode;
    }

    public void setRelationCode(String relationCode) {
        this.relationCode = relationCode;
    }
}
