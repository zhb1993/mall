<template>
<view>
<!--pages/submit-order/submit-order.wxml-->
<view class="container">
  <view class="submit-order">
    <!-- 收货地址 -->
<!--    <view class="delivery-addr " @tap="toAddrListPage">-->
<!--      <view class="addr-bg " v-if="!userAddr">-->
<!--        <view class="add-addr">-->
<!--          <view class="plus-sign-img">-->
<!--            <image src="/static/images/icon/plus-sign.png"></image>-->
<!--          </view>-->
<!--          <text>Añadir Dirección</text>-->
<!--        </view>-->
<!--        <view class="arrow empty"></view>-->
<!--      </view>-->
<!--      <view class="addr-bg whole" v-if="userAddr">-->
<!--        <view class="addr-icon">-->
<!--          <image src="/static/images/icon/addr.png"></image>-->
<!--        </view>-->
<!--        <view class="user-info">-->
<!--          <text class="item">{{userAddr.receiver}}</text>-->
<!--          <text class="item">{{userAddr.mobile}}</text>-->
<!--        </view>-->
<!--        <view class="addr">{{userAddr.province}}{{userAddr.city}}{{userAddr.area}}{{userAddr.addr}}</view>-->
<!--        <view class="arrow"></view>-->
<!--      </view>-->
<!--    </view>-->

    <!-- 商品详情 -->
    <view class="prod-item">
      <block v-for="(item, index) in orderItems" :key="index">
        <view class="item-cont" @tap="toOrderDetailPage" :data-ordernum="item.primaryOrderNo">
          <view class="prod-pic">
            <image :src="item.pic"></image>
          </view>
          <view class="prod-info">
            <view class="prodname">
              {{item.prodName}}
            </view>
            <view class="prod-info-cont">{{item.skuName}}</view>
            <view class="price-nums">
              <text class="prodprice"><text class="symbol">￥</text>
              <text class="big-num">{{wxs.parsePrice(item.price)[0]}}</text>
              <text class="small-num">.{{wxs.parsePrice(item.price)[1]}}</text></text>
              <text class="prodcount">x{{item.prodCount}}</text>
            </view>
          </view>
        </view>
      </block>
      <!-- <view class='item-cont' bindtap='toOrderDetailPage' data-ordernum="{{item.primaryOrderNo}}">
        <view class='prod-pic'>
          <image src='../../images/prod/pic09.jpg'></image>
        </view>
        <view class='prod-info'>
          <view class='prodname'>
            THE BEAST/野兽派 易烊千玺同款
          </view>
          <view class='prod-info-cont'>经典杯型升级，杯型更细长优雅</view>
          <view class='price-nums'>
            <text class='prodprice'><text class='symbol'>￥</text>
            <text class='big-num'>{{wxs.parsePrice(40.00)[0]}}</text>
            <text class='small-num'>.{{wxs.parsePrice(40.00)[1]}}</text></text>
            <text class="prodcount">x1</text>
          </view>
        </view>
      </view> -->

      <view class="total-num">
        <text class="prodcount">Total de{{totalCount}}Productos</text>
        <view class="prodprice">Todo：
          <text class="symbol">$</text>
          <text class="big-num">{{wxs.parsePrice(total)[0]}}</text>
          <text class="small-num">.{{wxs.parsePrice(total)[1]}}</text>
        </view>
      </view>
    </view>

    <!-- 订单详情 -->
    <view class="order-msg">
      <view class="msg-item">
        <view class="item">
          <text>Mensaje：</text>
          <input v-model="remarks" placeholder="Escriba el Mensaje" />
        </view>
      </view>

    </view>

    <view class="order-msg">
      <view class="msg-item">
        <view class="item">
          <view class="item-tit">Suma Total：</view>
          <view class="item-txt price">
            <text class="symbol">$</text>
            <text class="big-num">{{wxs.parsePrice(total)[0]}}</text>
            <text class="small-num">.{{wxs.parsePrice(total)[1]}}</text>
          </view>
        </view>
        <view class="item">
          <view class="item-tit">Costo de transporte:</view>
          <view class="item-txt price">
            <text class="symbol">$</text>
            <text class="big-num">{{wxs.parsePrice(transfee)[0]}}</text>
            <text class="small-num">.{{wxs.parsePrice(transfee)[1]}}</text>
          </view>
        </view>
        <view class="item">
          <view class="item-tit">Precio de Oferta:</view>
          <view class="item-txt price">
            <text class="symbol">-$</text>
            <text class="big-num">{{wxs.parsePrice(shopReduce)[0]}}</text>
            <text class="small-num">.{{wxs.parsePrice(shopReduce)[1]}}</text>
          </view>
        </view>
        <view class="item payment">
          <view class="item-txt price">
            Cantidad Total：
            <text class="symbol">$</text>
            <text class="big-num">{{wxs.parsePrice(actualTotal)[0]}}</text>
            <text class="small-num">.{{wxs.parsePrice(actualTotal)[1]}}</text>
          </view>
        </view>
      </view>
    </view>
  </view>


  <!-- 底部栏 -->
  <view class="submit-order-footer">
    <view class="sub-order">
      <view class="item-txt">
        Todo：
        <view class="price">
          <text class="symbol">￥</text>
          <text class="big-num">{{wxs.parsePrice(actualTotal)[0]}}</text>
          <text class="small-num">.{{wxs.parsePrice(actualTotal)[1]}}</text>
        </view>
      </view>
    </view>
    <view class="footer-box" @tap="inputDialogToggle">
      Enviar orden
    </view>
    <view>
      <!-- 输入框示例 -->
      <uni-popup ref="inputDialog" type="dialog">
        <uni-popup-dialog ref="inputClose"  mode="input" title="Ingrese su contraseña" value=""
                          placeholder="Ingrese su contraseña" @confirm="dialogInputConfirm" confirmText="Si" cancelText="No"></uni-popup-dialog>
      </uni-popup>
    </view>
  </view>
</view>

<!-- 选择优惠券弹窗 -->
<view class="popup-hide" v-if="popupShow">
  <view class="popup-box">
    <view class="popup-tit">
      <text>Cupones</text>
      <text class="close" @tap="closePopup"></text>
    </view>
    <view class="coupon-tabs">
      <view :class="'coupon-tab ' + (couponSts==1?'on':'')" @tap="changeCouponSts" data-sts="1">Cupones Disponibles({{coupons.canUseCoupons.length?coupons.canUseCoupons.length:0}})</view>
      <view :class="'coupon-tab ' + (couponSts==2?'on':'')" @tap="changeCouponSts" data-sts="2">Cupones no Disponibles({{coupons.unCanUseCoupons.length?coupons.unCanUseCoupons.length:0}})</view>
    </view>
    <view class="popup-cnt">
      <block v-for="(item, index) in coupons.canUseCoupons" :key="index" v-if="couponSts == 1">
        <coupon :item="item" order="true" @checkCoupon="checkCoupon" canUse="true"></coupon>
      </block>
      <block v-for="(item, index) in coupons.unCanUseCoupons" :key="index" v-if="couponSts == 2">
        <coupon :item="item" order="true" canUse="false"></coupon>
      </block>
      <view class="botm-empty"></view>
    </view>
    <view class="coupon-ok" v-if="couponSts==1">
      <text @tap="choosedCoupon">Confirmación</text>
    </view>
  </view>
</view>
</view>
</template>

<script module="wxs" lang="wxs" src="../../wxs/number.wxs"></script>

<script>
// pages/submit-order/submit-order.js
import {encrypt} from "../../utils/crypto";

var http = require("../../utils/http.js");
import coupon from "../../components/coupon/coupon";
export default {
  data() {
    return {
      popupShow: false,
      couponSts: 1,
      couponList: [],
      // 订单入口 0购物车 1立即购买
      orderEntry: "0",
      userAddr: null,
      orderItems: [],
      coupon: {
        totalLength: 0,
        canUseCoupons: [],
        noCanUseCoupons: []
      },
      actualTotal: 0,
      total: 0,
      totalCount: 0,
      transfee: 0,
      reduceAmount: 0,
      remarks: "",
      couponIds: [],
      coupons: {},
      shopReduce: "",
      item: {},
      selAddress: ''
    };
  },

  components: {
    coupon,
  },
  props: {},

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      orderEntry: options.orderEntry
    });
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {},

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    var pages = getCurrentPages();
    var currPage = pages[pages.length - 1];
    if (currPage.selAddress == "yes") {
      //将携带的参数赋值
      this.userAddr = currPage.item
    }

    //获取订单数据
    this.loadOrderData();
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {},

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {},

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {},

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {},

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {},
  methods: {
    //加载订单数据
    loadOrderData: function () {
      var addrId = 0;

      if (this.userAddr != null) {
        addrId = this.userAddr.addrId;
      }

      uni.showLoading({
        mask: true
      });
      var params = {
        url: "/p/order/confirm",
        method: "POST",
        data: {
          addrId: addrId,
          orderItem: this.orderEntry === "1" ? JSON.parse(uni.getStorageSync("orderItem")) : undefined,
          basketIds: this.orderEntry === "0" ? JSON.parse(uni.getStorageSync("basketIds")) : undefined,
          couponIds: this.couponIds,
          userChangeCoupon: 1
        },
        callBack: res => {
          uni.hideLoading();
          let orderItems = [];
          res.shopCartOrders[0].shopCartItemDiscounts.forEach(itemDiscount => {
            orderItems = orderItems.concat(itemDiscount.shopCartItems);
          });

          if (res.shopCartOrders[0].coupons) {
            let canUseCoupons = [];
            let unCanUseCoupons = [];
            res.shopCartOrders[0].coupons.forEach(coupon => {
              if (coupon.canUse) {
                canUseCoupons.push(coupon);
              } else {
                unCanUseCoupons.push(coupon);
              }
            });
            this.setData({
              coupons: {
                totalLength: res.shopCartOrders[0].coupons.length,
                canUseCoupons: canUseCoupons,
                unCanUseCoupons: unCanUseCoupons
              }
            });
          }

          this.setData({
            orderItems: orderItems,
            actualTotal: res.actualTotal,
            total: res.total,
            totalCount: res.totalCount,
            userAddr: res.userAddr,
            transfee: res.shopCartOrders[0].transfee,
            shopReduce: res.shopCartOrders[0].shopReduce
          });
        },
        errCallBack: res => {
          uni.hideLoading();
          this.chooseCouponErrHandle(res);
        }
      };
      http.request(params);
    },

    /**
     * 优惠券选择出错处理方法
     */
    chooseCouponErrHandle(res) {
      // 优惠券不能共用处理方法
      if (res.statusCode == 601) {
        uni.showToast({
          title: res.data,
          icon: "none",
          duration: 3000,
          success: res => {
            this.setData({
              couponIds: []
            });
          }
        });
        setTimeout(() => {
          this.loadOrderData();
        }, 2500);
      }
    },
    inputDialogToggle() {
      this.$refs.inputDialog.open()
    },
    dialogInputConfirm(val) {
      this.toPay(val);

      setTimeout(() => {
        uni.hideLoading()
        console.log(val)
        // 关闭窗口后，恢复默认内容
        this.$refs.inputDialog.close()
      }, 3000)
    },

    /**
     * 提交订单
     */
    toPay: function (value) {
      // if (!this.userAddr) {
      //   uni.showToast({
      //     title: 'Seleccionar Dirección',
      //     icon: "none"
      //   });
      //   return;
      // }
      this.submitOrder(encrypt(value));
    },
    submitOrder: function (password) {
      uni.showLoading({
        mask: true
      });
      var params = {
        url: "/p/order/submit",
        method: "POST",
        data: {
          orderShopParam: [{
            remarks: this.remarks,
            shopId: 1
          }]
        },
        callBack: res => {
					console.log("res",res)
          uni.hideLoading();
          // this.calWeixinPay(res.orderNumbers);
					this.normalPay(res.orderNumbers,password)

        }
      };
      http.request(params);
    },
		
		//模拟支付，直接提交成功
		normalPay: function(orderNumbers,password){
			uni.showLoading({
			  mask: true
			});
			var params = {
			  url: "/p/order/normalPay",
			  method: "POST",
			  data: {
			    orderNumbers: orderNumbers,
          password: password
			  },
			  callBack: res => {
					console.log("res",res)
					uni.hideLoading();
					if(res){
						uni.showToast({
							title: "Pago Exitoso",
							icon:"none"
						})
						setTimeout(() => {
							uni.navigateTo({
							  url: '/pages/pay-result/pay-result?sts=1&orderNumbers=' + orderNumbers
							});
						},1200)
					}else{
						uni.showToast({
							title: "Fallo de Pago！",
							icon:"none"
						})
					}
			  }
			};
			http.request(params);
		},
		
    /**
     * 唤起微信支付
     */
    calWeixinPay: function (orderNumbers) {
      uni.showLoading({
        mask: true
      });
      var params = {
        url: "/p/order/pay",
        method: "POST",
        data: {
          payType: 1,
          orderNumbers: orderNumbers
        },
        callBack: function (res) {
          uni.hideLoading();
          uni.requestPayment({
            timeStamp: res.timeStamp,
            nonceStr: res.nonceStr,
            package: res.packageValue,
            signType: res.signType,
            paySign: res.paySign,
            success: e => {
              // console.log("支付成功");
              uni.navigateTo({
                url: '/pages/pay-result/pay-result?sts=1&orderNumbers=' + orderNumbers + "&orderType=" + this.orderType
              });
            },
            fail: err => {
              uni.navigateTo({
                url: '/pages/pay-result/pay-result?sts=0&orderNumbers=' + orderNumbers + "&orderType=" + this.orderType
              });
            }
          });
        }
      };
      http.request(params);
    },
    changeCouponSts: function (e) {
      this.setData({
        couponSts: e.currentTarget.dataset.sts
      });
    },
    showCouponPopup: function () {
      this.setData({
        popupShow: true
      });
    },
    closePopup: function () {
      this.setData({
        popupShow: false
      });
    },
    closeComment: function () {
      this.setData({
        commentShow: false
      });
    },

    /**
     * 去地址页面
     */
    toAddrListPage: function () {
      uni.navigateTo({
        url: '/pages/delivery-address/delivery-address?order=0'
      });
    },

    /**
     * 确定选择好的优惠券
     */
    choosedCoupon: function () {
      this.loadOrderData();
      this.setData({
        popupShow: false
      });
    },

    confirmPassword: function () {
      this.setData({
        commentShow: true
      });
    },

    /**
     * 优惠券子组件发过来
     */
    checkCoupon: function (e) {
      var ths = this;
      let index = ths.couponIds.indexOf(e.detail.couponId);

      if (index === -1) {
        ths.couponIds.push(e.detail.couponId);
      } else {
        ths.couponIds.splice(index, 1);
      }
    }
  }
};
</script>
<style>
@import "./submit-order.css";

.dialog,
.share {
  /* #ifndef APP-NVUE */
  display: flex;
  /* #endif */
  flex-direction: column;
}

.dialog-box {
  padding: 10px;
}

.dialog .button,
.share .button {
  /* #ifndef APP-NVUE */
  width: 100%;
  /* #endif */
  margin: 0;
  margin-top: 10px;
  padding: 3px 0;
  flex: 1;
}

.dialog-text {
  font-size: 14px;
  color: #333;
}
</style>