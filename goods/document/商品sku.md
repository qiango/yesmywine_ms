接口清单：
- [查看所有sku](#查看所有sku)
- [同步sku](#同步sku)


#### 查看所有sku

##### url: `http://47.89.18.26:8990/goods/sku`
请求方式 : `GET`

参数名(参数名后追加“_l”为模糊查询)    | 含义    | 是否必须
-------|--------|-----
skuId| id |N（查详情）
showField |显示需要查询的字段（例如：showField=skuName,code）｜N
pageNo|  分页页号 |N
pageSize| 分页大小 |N
sku_l  |sku名字｜N（参数后加l为模糊查询）
code_l |SKU编码｜N
categoryId |分类Id｜N
supplierId |供应商Id｜N
all| 是否显示全部数据 （true-是，false-否，默认为false） |N

###  返回值

参数名  | 含义
-------------|-------------
List<Sku>  |sku实体
```json
{
    "code":"200",
    "msg":"success",
    "data":{
        "page":0,
        "totalPages":1,
        "pageSize":10,
        "totalRows":3,
        "content":[
            {
                    "code": "0A68YKZE633ZYU011qB2W0801",
                    "skuName": "五粮液",
                    "isUse": "no",
                    "type": 0,
                    "property": "{\"产地\":\"中国\",\"品牌\":\"妙高山\",\"品相\":\"残次\",\"容量\":\"1.5KG\",\"年份\":\"1968\",\"度数\":\"54度\",\"是否自采\":\"也买供货\",\"组合数\":\"单瓶\"}",
                    "sku": "五粮液 也买供货 1968 54度 1.5KG 中国 残次 妙高山 单瓶",
                    "supplier": {
                      "supplierName": "海淘",
                      "supplierCode": "qB2",
                      "supplierType": "seaAmoy",
                      "provinceId": 14,
                      "province": "山西",
                      "cityId": 1404,
                      "city": "长治",
                      "areaId": 140421,
                      "area": "长治县",
                      "address": "陕西省长治市",
                      "postCode": "222222",
                      "contact": "杨明倩",
                      "telephone": "62011111",
                      "mobilePhone": "13811111111",
                      "fax": "021-111111",
                      "mailbox": "ymq@hello.com",
                      "grade": "一级",
                      "accountNumber": "ymq",
                      "credit": "良好",
                      "procurementCycl": "1个月",
                      "paymentType": "现金",
                      "invoiceCompany": "博彦科技",
                      "primarySupplier": "供应商1",
                      "merchantIdentification": null,
                      "productManager": "杨瑞风",
                      "bank": "农商行",
                      "bankAccount": "6200100010001",
                      "dutyParagraph": "sx001",
                      "paymentDays": "Jan 3, 2017 12:03:00 AM",
                      "deleteEnum": "NOT_DELETE",
                      "id": 1,
                      "createTime": "May 23, 2017 6:57:58 AM"
                    },
                    "category": {
                      "id": 7,
                      "categoryName": "保健酒",
                      "code": "W0801",
                      "deleteEnum": "NOT_DELETE",
                      "isShow": "yes",
                      "image": "[{'id':'393','name':'e5c1c9933dd02315eb9b1ea2a7e050ce'}]",
                      "parentName": {
                        "id": 2,
                        "categoryName": "白酒",
                        "code": "X0006",
                        "deleteEnum": "NOT_DELETE",
                        "isShow": "yes",
                        "image": "[{'id':'391','name':'ddd6e81dd4f6aa3f550051733929a2c8'}]",
                        "parentName": {
                          "id": 1,
                          "categoryName": "酒",
                          "code": "X0001",
                          "deleteEnum": "NOT_DELETE",
                          "isShow": "yes",
                          "image": "[{'id':'390','name':'74c96d6aa39c2bb204a261e3a36fd42e'}]",
                          "parentName": null,
                          "propertyInfo": null,
                          "level": 1,
                          "createTime": "May 23, 2017 6:24:09 AM"
                        },
                        "propertyInfo": null,
                        "level": 2,
                        "createTime": "May 23, 2017 6:28:17 AM"
                      },
                      "propertyInfo": null,
                      "level": 3,
                      "createTime": "May 23, 2017 6:48:47 AM"
                    },
                    "id": 9,
                    "createTime": "May 24, 2017 6:27:04 AM"
                  }
        ],
        "hasPrevPage":false,
        "hasNextPage":false,
        "url":null,
        "conditionJson":null,
        "fields":null
    }
}
```

----------------------------------------

#### 同步sku

##### url: 
请求方式 : 

参数名    | 含义
-------|--------
code|SKU编码
skuName|名称
property|属性
supplier|渠道
category|分类
###  返回值

参数名  | 含义
-------------|-------------

----------------------------------------
