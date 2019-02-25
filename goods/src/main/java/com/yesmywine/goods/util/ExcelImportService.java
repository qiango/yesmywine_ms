//package com.yesmywine.goods.util;
//
//import com.yesmywine.goods.dao.BrandDao;
//import com.yesmywine.goods.dao.GoodsDao;
//import com.yesmywine.goods.entity.Goods;
//import com.yesmywine.goods.service.Impl.BrandSrviceImpl;
//import com.yesmywine.util.basic.MapUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//
//@Service
//public class ExcelImportService {
//    @Autowired
//    private GoodsDao goodsRepository;
//    @Autowired
//    private BrandDao brandRepository;
////    @Autowired
////    private CategoryServiceImpl categoryService;
//    @Autowired
//    private BrandSrviceImpl brandService;
//
//
//    public boolean importGoods(String excelFilePath) {
//        String keyName = "goodsNumber,categoryName,brandName,goodsName,goodsTitle,statusName";
//        String keyType = "string,string,string,string,string,string";
//        Integer startCol = 0;
//        Integer startRow = 2;
//        List<Goods> listGoods = new ArrayList<>();
//        List<Map<String, Object>> list = MSExcel.parseExcel(excelFilePath, keyName, keyType, startRow, startCol);
//        for (Map<String, Object> map : list) {
//            System.out.println("goods:" + map.get("goodsName"));
//            Goods goods = (Goods) MapUtil.mapToObject(map, Goods.class);
//
////			goods.trimGoodsNumber();
////			Integer categoryId=CategoryService.find(goods.getCategoryName());
////			goods.setCategoryId(categoryId);
////			Integer brandId=brandService.find(goods.getBrandName());
////			goods.setBrandId(brandId);
////			goods.setStatus(Util.getStatus(goods.getStatusName()));
////			goods.setType(GoodsType.normalGoods);
////            goods.setDeleteEnum(DeleteEnum.NOT_DELETE);
//            goods.setCreateTime(new Date());
//            listGoods.add(goods);
//        }
//        goodsRepository.save(listGoods);
//        return true;
//    }
//
//}
