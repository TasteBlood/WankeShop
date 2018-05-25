package com.cloudcreativity.wankeshop.goods;

import android.content.Context;
import android.databinding.ObservableField;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.cloudcreativity.wankeshop.R;
import com.cloudcreativity.wankeshop.base.BaseBannerImageHolder;
import com.cloudcreativity.wankeshop.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.wankeshop.base.BaseDialogImpl;
import com.cloudcreativity.wankeshop.databinding.FragmentGoodsIndexBinding;
import com.cloudcreativity.wankeshop.databinding.ItemGoodsSizeBinding;
import com.cloudcreativity.wankeshop.entity.AddressEntity;
import com.cloudcreativity.wankeshop.entity.GoodsEntity;
import com.cloudcreativity.wankeshop.utils.ChooseReceiveAddressDialogUtils;
import com.cloudcreativity.wankeshop.utils.DefaultObserver;
import com.cloudcreativity.wankeshop.utils.HttpUtils;
import com.cloudcreativity.wankeshop.utils.SPUtils;
import com.cloudcreativity.wankeshop.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 这是GoodsIndexFragment ViewModal
 */
public class GoodsIndexViewModal {
    public BaseBindingRecyclerViewAdapter<Map<String,Object>,ItemGoodsSizeBinding> sizeAdapter;

    private BaseDialogImpl baseDialog;
    private Context context;
    private FragmentGoodsIndexBinding binding;
    private ChooseReceiveAddressDialogUtils utils;//这是选择地址的对话框工具
    //这是展示的数据信息
    public ObservableField<String> price = new ObservableField<>();//这是价格

    public ObservableField<GoodsEntity> goods = new ObservableField<>();//默认的商品信息

    public ObservableField<String> selectInfo = new ObservableField<>();//这是已选信息

    public ObservableField<Integer> selectNumber = new ObservableField<>();//这是已选数量

    public ObservableField<String> numberLimit = new ObservableField<>();//这是数量限制

    public ObservableField<Boolean> isGift = new ObservableField<>();//这是是否促销的商品

    public ObservableField<String> giftsInfo = new ObservableField<>();//促销商品的信息

    //这是规格数据
    private List<Map<String,Object>> dataMaps = new ArrayList<>();

    //用来处理规格操作
    private Map<String,Object> lastMaps;
    private int lastPosition;

    GoodsIndexViewModal(BaseDialogImpl baseDialog,Context context,GoodsEntity entity,FragmentGoodsIndexBinding binding) {
        this.context = context;
        this.baseDialog = baseDialog;
        this.context = context;
        this.goods.set(entity);
        this.binding = binding;
        //初始化信息
        selectNumber.set(0);

        String string = SPUtils.get().getString(SPUtils.Config.DEFAULT_ADDRESS, null);
        if(!TextUtils.isEmpty(string))
            binding.tvGoodsIndexAddress.setText(new Gson().fromJson(string,AddressEntity.class).formatAddress());

        sizeAdapter = new BaseBindingRecyclerViewAdapter<Map<String, Object>, ItemGoodsSizeBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_goods_size;
            }

            @Override
            protected void onBindItem(ItemGoodsSizeBinding binding, final Map<String, Object> item, final int position) {
                binding.setMaps(item);
                binding.rbGoodsItemSize.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(item.containsKey("skuId")){
                            //说明是有规格的数据
                            if(lastMaps!=null&&lastPosition>=0)
                                lastMaps.put("isCheck",false);
                            item.put("isCheck",true);

                            sizeAdapter.notifyItemChanged(lastPosition);
                            sizeAdapter.notifyItemChanged(position);

                            selectInfo.set(format_size((GoodsEntity.SKU) item.get("sizes")));
                            selectNumber.set(Integer.valueOf(String.valueOf(item.get("minNumber"))));
                            price.set(String.format(context.getResources().getString(R.string.str_rmb_character),Float.parseFloat(String.valueOf(item.get("salePrice")))));

                            lastPosition = position;
                            lastMaps = item;
                        }else{
                            item.put("isCheck",true);
                            sizeAdapter.notifyItemChanged(position);
                            selectInfo.set(format_size((GoodsEntity.SKU) item.get("sizes")));
                            selectNumber.set(0);
                        }

                        isGift.set(Boolean.valueOf(String.valueOf(lastMaps.get("isGift"))));
                        //展示促销信息
                        if(lastMaps.containsKey("gifts")){
                            if(lastMaps.get("gifts")!=null){
                                List<GoodsEntity.SKUGift> gifts = (List<GoodsEntity.SKUGift>) lastMaps.get("gifts");
                                if(gifts!=null&&!gifts.isEmpty()){
                                    StringBuilder builder = new StringBuilder();
                                    for(GoodsEntity.SKUGift gift:gifts){
                                        builder.append("满").append(gift.getSkuNum()).append("送").append(gift.getGiftNum()).append(",");
                                    }
                                    giftsInfo.set(builder.substring(0,builder.length()-1));
                                }
                            }
                        }
                        formatNumberLimit(lastMaps.get("minNumber").toString(),lastMaps.get("maxNumber").toString(),lastMaps.get("limit").toString(),lastMaps.get("total").toString());

                    }
                });
            }
        };

        //展示banner信息
        displayBanner(entity);
        //接下来就是展示规格信息
        displaySize(entity);
    }

    /**
     * 选择收货地址
     */
    public void onChooseAddressClick(View view){
        HttpUtils.getInstance().getMyAddress()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String t) {
                        Type type = new TypeToken<List<AddressEntity>>() {
                        }.getType();
                        List<AddressEntity> addressEntities = new Gson().fromJson(t,type);
                        utils = new ChooseReceiveAddressDialogUtils(context);
                        utils.show(addressEntities);
                        utils.setOnItemClickListener(new ChooseReceiveAddressDialogUtils.OnItemClickListener() {
                            @Override
                            public void onItemClick(AddressEntity entity) {
                                binding.tvGoodsIndexAddress.setText(entity.formatAddress());
                                //在这里选择完地址之后，将默认的地址存储在本地，方便在下次使用时直接显示
                                String toJson = new Gson().toJson(entity);
                                SPUtils.get().putString(SPUtils.Config.DEFAULT_ADDRESS,toJson);
                            }
                        });
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }

    /**
     *
     * @param goodsEntity 数据实体
     *                    展示商品规格
     */
    private void displaySize(GoodsEntity goodsEntity) {
        if(goodsEntity.getSkus()!=null&&!goodsEntity.getSkus().isEmpty()){
            //说明规格不是空的，然后遍历规格列表，然后组合出所有的规格，并且显示，选中默认的第一个
            for(GoodsEntity.SKU sku : goodsEntity.getSkus()){
                //开始遍历
                Map<String,Object> dataMap = new HashMap<>();
                StringBuilder builder = new StringBuilder();
                dataMap.put("salePrice",sku.getSalePrice());
                dataMap.put("skuId",sku.getId());
                dataMap.put("isCheck",false);
                dataMap.put("isGift",sku.getIsGift()==1);//是否是促销商品
                dataMap.put("gifts",sku.getSkuGiftList());//促销信息
                dataMap.put("minNumber",sku.getMinQuality());
                dataMap.put("maxNumber",sku.getMaxQuality());
                dataMap.put("limit",sku.getRegionOperator());//这是表示配送数量的范围
                dataMap.put("total",sku.getDepositNum());//这是库存数量
                for(int i=0;i<sku.getSpecs().size();i++){
                    builder.append(sku.getSpecs().get(i).getGoodsSpeciValue())
                            .append("+");
                }
                dataMap.put("content",builder.subSequence(0,builder.length()-1).toString());
                dataMap.put("sizes",sku);
                dataMaps.add(dataMap);
            }
        }else{
            Map<String,Object> dataMap = new HashMap<>();
            dataMap.put("content","暂无规格");
            dataMap.put("sizes",null);
            dataMap.put("minNumber",1);
            dataMap.put("maxNumber",200);
            dataMap.put("limit","-");
            dataMap.put("total",200);
            dataMap.put("isGift",false);
            dataMap.put("gifts",null);
            dataMap.put("salePrice",goodsEntity.getSalePrice());
            dataMaps.add(dataMap);
        }

        //默认显示第一个规格的信息
        dataMaps.get(0).put("isCheck",true);

        lastPosition = 0;
        lastMaps = dataMaps.get(0);

        selectInfo.set(format_size((GoodsEntity.SKU) lastMaps.get("sizes")));
        selectNumber.set(Integer.parseInt(String.valueOf(lastMaps.get("minNumber"))));
        price.set(String.format(context.getResources().getString(R.string.str_rmb_character),
                Float.parseFloat(String.valueOf(lastMaps.get("salePrice")))));

        //创建数量限制
        formatNumberLimit(lastMaps.get("minNumber").toString(),lastMaps.get("maxNumber").toString(),lastMaps.get("limit").toString(),String.valueOf(lastMaps.get("total")));

        //显示是否是促销
        isGift.set(Boolean.valueOf(String.valueOf(lastMaps.get("isGift"))));
        //展示促销信息
        if(lastMaps.containsKey("gifts")){
            if(lastMaps.get("gifts")!=null){
                List<GoodsEntity.SKUGift> gifts = (List<GoodsEntity.SKUGift>) lastMaps.get("gifts");
                if(gifts!=null&&!gifts.isEmpty()){
                    StringBuilder builder = new StringBuilder();
                    for(GoodsEntity.SKUGift gift:gifts){
                        builder.append("满").append(gift.getSkuNum()).append("送").append(gift.getGiftNum()).append(",");
                    }
                    giftsInfo.set(builder.substring(0,builder.length()-1));
                }
            }
        }
        sizeAdapter.getItems().addAll(dataMaps);

        binding.rcvGoodsIndexSize.setAdapter(sizeAdapter);

        binding.tvNumber.addTextChangedListener(numberWatcher);

    }

    //格式化配送数量区间
    private void formatNumberLimit(String minNumber,String maxNumber,String limitRegion,String total) {
        if("-".equals(limitRegion)){
            //说明是最小范围和最大范围间配送
            //还需要判断是否最大库存小于最大配送数
            if(Integer.parseInt(maxNumber)>Integer.parseInt(total)){
                numberLimit.set(String.format(context.getResources().getString(R.string.str_number_limit_min),minNumber,goods.get().getUnit().getUnityName())
                        .concat(String.format(context.getResources().getString(R.string.str_number_limit_max),total)));
            }else{
                numberLimit.set(String.format(context.getResources().getString(R.string.str_number_limit_min),minNumber,goods.get().getUnit().getUnityName())
                        .concat(String.format(context.getResources().getString(R.string.str_number_limit_max),maxNumber)));
            }
        }else if(">=".equals(limitRegion)){
            //大于最小配送范围
            numberLimit.set(String.format(context.getResources().getString(R.string.str_number_limit_min),minNumber,goods.get().getUnit().getUnityName())
                    .concat(String.format(context.getResources().getString(R.string.str_number_limit_max),total)));
        }else{
            numberLimit.set(String.format(context.getResources().getString(R.string.str_number_limit_min),minNumber,goods.get().getUnit().getUnityName()));
        }
    }

    /**
     * 展示banner图片信息
     */
    private void displayBanner(GoodsEntity entity){
        List<String> images = new ArrayList<>();
        images.add(entity.getSpuPic());
        for(GoodsEntity.SKU sku:entity.getSkus()){
            if(!TextUtils.isEmpty(sku.getIcon()))
                images.add(sku.getIcon());
        }
        //显示
        binding.bannerGoodsIndex.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new BaseBannerImageHolder();
            }
        },images)
                //设置指示器是否可见
                .setPointViewVisible(true)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.banner_dot_5dp_normal, R.drawable.banner_dot_5dp})
                //设置指示器的方向（左、中、右）
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                //设置手动影响（设置了该项无法手动切换）
                .setManualPageable(true);

    }

    //数量增加的方法
    public void onPlus(View view){
        if(lastMaps==null)
            return;
        int i = selectNumber.get().intValue();
        if(">=".equals(lastMaps.get("limit").toString())){
            //说明是库存
            if(i>=Integer.parseInt(lastMaps.get("total").toString())){
                return;
            }
        }else{
            if(i>=Integer.parseInt(lastMaps.get("maxNumber").toString())){
                return;
            }
        }
        i++;
        selectNumber.set(i);
    }

    //数量减少的方法
    public void onMinus(View view){
        if(lastMaps==null)
            return;
        int i = selectNumber.get().intValue();
        if(i<=Integer.parseInt(String.valueOf(lastMaps.get("minNumber"))))
            return;
        i--;
        selectNumber.set(i);
    }

    //加入购物车操作
    public Map<String,Object> onAddShopCar(){
        if(lastMaps==null){
            ToastUtils.showShortToast(context,"请选择规格");
            return null;
        }
        int number = selectNumber.get();
        //配送数量为0
        if(number<=0){
            ToastUtils.showShortToast(context,"数量不符合配送范围");
            return null;
        }
        //配送区间时
        if("-".equals(lastMaps.get("limit"))){
            if(number<Integer.parseInt(lastMaps.get("minNumber").toString())||
                    number>Integer.parseInt(lastMaps.get("maxNumber").toString())){
                ToastUtils.showShortToast(context,"配送数量不符合");
                return null;
            }
        }else if(">=".equals(lastMaps.get("limit"))){
            if(number<Integer.parseInt(lastMaps.get("minNumber").toString())||
                    number>Integer.parseInt(lastMaps.get("total").toString())){
                ToastUtils.showShortToast(context,"配送数量不符合");
                return null;
            }
        }
        Map<String,Object> finalData = new HashMap<>();
        finalData.put("spuId",goods.get().getId());//这是商品的id
        finalData.put("skuId",lastMaps.get("skuId"));//这是规格的id
        finalData.put("number",number);//这是数量
        return finalData;
    }

    //数量监听
    private TextWatcher numberWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(lastMaps==null)
                return;
            if(TextUtils.isEmpty(s)){
                selectNumber.set(0);
                return;
            }
            int i = Integer.parseInt(s.toString());
            selectNumber.set(i);
            binding.tvNumber.setSelection(s.length());
        }
    };


    public void refreshAddress(){
        //if(utils!=null)
           // utils.updateData(baseDialog,);
    }

    //格式化规格信息
    private String format_size(GoodsEntity.SKU sku){
        if(sku==null){
            return "暂无规格";
        }else{
            StringBuilder builder = new StringBuilder();
            for(int i=0;i<sku.getSpecs().size();i++){
                builder.append(sku.getSpecs().get(i).getSpecifactionName())
                        .append(" ")
                        .append(sku.getSpecs().get(i).getGoodsSpeciValue())
                        .append("+");
            }
            return builder.substring(0,builder.length()-1);
        }
    }
}
