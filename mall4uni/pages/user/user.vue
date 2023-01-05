<template>
	<!--pages/user/user.wxml-->
	<view class="container">
		<!-- 用户信息 -->
		<!-- # indef MP-wei -->
		<!-- <view class="userinfo">
    <view class="userinfo-avatar">
      <open-data type="userAvatarUrl"></open-data>
    </view>
    <view class="userinfo-name">
      <open-data type="userNickName"></open-data>
    </view>
  </view>
  <view class="binding-phone">
    <text class="show-tip">绑定手机号后可查看订单和领取优惠券，</text>
    <text class="gotobinding" @tap="toBindingPhone">去绑定</text>
  </view> -->

		<view class="userinfo" v-if="isAuthInfo">
			<view class="userinfo-con">
				<view class="userinfo-avatar">
					<!-- <open-data type="userAvatarUrl"></open-data> -->
					<image :src="user.pic ? (user.pic.indexOf('http') === -1 ? picDomain + user.pic : user.pic) : '../../static/images/icon/head04.png'"></image>
				</view>
				<view class="userinfo-name">
					<view>{{user.nickName ? user.nickName : "Nombre de Usuario"}}</view>
					<!-- <open-data type="userNickName"></open-data> -->
				</view>
			</view>
		</view>

		<view class="userinfo-none" v-if="!isAuthInfo">
			<view class="default-pic" @tap="toLogin">
				<image src="../../static/images/icon/head04.png"></image>
			</view>
			<view class="none-login" @tap="toLogin">
				<button class="unlogin">No ha iniciado sesion</button>
				<button class="click-login">Cuenta de Inicio de sesion</button>
			</view>
		</view>
		<!-- end 用户信息 -->
<!--    <view class="my-menu">-->
<!--      <view class="memu-item">-->
<!--        <view class="i-name">-->
<!--      <uni-icons type="wallet" size="30" style="margin-right: 0px;margin-left: 20px"></uni-icons>-->
<!--        <uni-section class="mb-10" title="Saldo">-->
<!--          <template v-slot:right>-->
<!--            <view>-->
<!--            <text style="display: inline-block;width: 60vw;text-align: right;position: relative;left: 15vw;">{{ user.accountBalance ? user.accountBalance : 0.00 }}</text>-->
<!--            </view>-->
<!--          </template>-->
<!--        </uni-section>-->
<!--        </view>-->
<!--      </view>-->
<!--    </view>-->

		<view class="list-cont">

			<!-- 订单状态 -->
			<view class="total-order">
				<view class="order-tit">
					<text style="font-weight:bold">Mis Ordenes</text>
					<view class="checkmore" @tap="toOrderListPage" data-sts="0">
						<text>Ver Todo </text>
						<text class="arrowhead"></text>
					</view>
				</view>
				<view class="procedure">
					<view class="items" @tap="toOrderListPage" data-sts="1">
						<image src="/static/images/icon/toPay.png"></image>
						<text>Pendiente de Pago</text>
						<text class="num-badge" v-if="orderAmount.unPay>0">{{orderAmount.unPay}}</text>
					</view>
					<view class="items" @tap="toOrderListPage" data-sts="2">
						<image src="/static/images/icon/toDelivery.png"></image>
						<text>Pendiente de Envío</text>
						<text class="num-badge" v-if="orderAmount.payed>0">{{orderAmount.payed}}</text>
					</view>
					<view class="items" @tap="toOrderListPage" data-sts="3">
						<image src="/static/images/icon/toTake.png"></image>
						<text>Pendiente de Recibir</text>
						<text class="num-badge" v-if="orderAmount.consignment>0">{{orderAmount.consignment}}</text>
					</view>
					<view class="items" @tap="toOrderListPage" data-sts="5">
						<image src="/static/images/icon/toComment.png"></image>
						<text>Completado</text>
					</view>
				</view>
			</view>
			<!--end 订单状态 -->

			<view class="prod-col">
				<view class="col-item" @tap="myCollectionHandle">
					<view v-if="loginResult" class="num">{{collectionCount}}</view>
					<view v-else class="num">--</view>
					<view class="tit">Favoritos</view>
				</view>
				<view class="col-item" @tap="handleTips">
					<view v-if="loginResult" class="num">5</view>
					<view v-else class="num">--</view>
					<view class="tit">Noticias</view>
				</view>
				<view class="col-item" @tap="handleTips">
					<view v-if="loginResult" class="num">3</view>
					<view v-else class="num">--</view>
					<view class="tit">Historial</view>
				</view>
			</view>
      <view class="my-menu">
        <view class="memu-item" @tap="">
          <view class="i-name">
            <image src="/static/images/icon/balance.png"></image>
            <text>Saldo</text>
          </view>
          <view>{{user ? user.accountBalance : '0.00'}}</view>
        </view>
      </view>

      <view class="my-menu">
        <view class="memu-item" @tap="inputDialogToggle">
          <view class="i-name">
            <image src="/static/images/icon/recovery.png"></image>
            <text>Reciclaje</text>
          </view>
          <view class="arrowhead"></view>
        </view>
      </view>
      <view>
        <!-- 输入框示例 -->
        <uni-popup ref="inputDialog" type="dialog">
          <uni-popup-dialog ref="inputClose"  mode="input" title="请输入订单编号" value=""
                            placeholder="请输入订单编号" @confirm="recoveryOrder"></uni-popup-dialog>
        </uni-popup>
      </view>

			<view class="my-menu">
<!--				<view class="memu-item" @tap="toDistCenter">
					<view class="i-name">
						<image src="/static/images/icon/promotion.png"></image>
						<text>分销中心</text>
					</view>
					<view class="arrowhead"></view>
				</view>-->


				<view class="memu-item" @tap="toAddressList">
					<view class="i-name">
						<image src="/static/images/icon/myAddr.png"></image>
						<text>Direccion</text>
					</view>
          <view class="arrowhead"></view>
				</view>
			</view>

			<!--end 列表项 -->

			<view class="log-out" @tap="logout" v-if="isAuthInfo">
				<view class="log-out-n">
					<text>Salida</text>
				</view>
			</view>
		</view>

	</view>
</template>

<script>
	// pages/user/user.js
	import {encrypt} from "../../utils/crypto";

  var http = require("../../utils/http.js");
	var util = require("../../utils/util.js");
	var config = require("../../utils/config.js");

	export default {
		data() {
			return {
				orderAmount: '',
				sts: '',
				collectionCount: 0,
				isAuthInfo: false,
				loginResult: {},
        user: {},
				picDomain: config.picDomain
			};
		},

		components: {},
		props: {},

		/**
		 * 生命周期函数--监听页面加载
		 */
		onLoad: function(options) {},

		/**
		 * 生命周期函数--监听页面初次渲染完成
		 */
		onReady: function() {},

		/**
		 * 生命周期函数--监听页面显示
		 */
		onShow: function() {
      //加载订单数字
      var ths = this; // var status = ths.data.status

      ths.setData({
        loginResult: uni.getStorageSync("loginResult"),
        // isAuthInfo: Boolean(wx.getStorageSync('loginResult').userId),
        // user: uni.getStorageSync("loginResult").user,
      });
      if (ths.loginResult) {
        ths.setData({
          isAuthInfo: true
        })
      } else {
        ths.setData({
          isAuthInfo: false
        })
      }
      if (ths.isAuthInfo) {
        uni.showLoading();
        var params = {
          url: "/p/myOrder/orderCount",
          method: "GET",
          data: {},
          callBack: function (res) {
            uni.hideLoading();
            ths.setData({
              orderAmount: res
            });
          }
        };
        http.request(params);
        this.showCollectionCount();
        this.getUserInfo();
      }
    },

		/**
		 * 生命周期函数--监听页面隐藏
		 */
		onHide: function() {},

		/**
		 * 生命周期函数--监听页面卸载
		 */
		onUnload: function() {},

		/**
		 * 页面相关事件处理函数--监听用户下拉动作
		 */
		onPullDownRefresh: function() {},

		/**
		 * 页面上拉触底事件的处理函数
		 */
		onReachBottom: function() {},

		/**
		 * 用户点击右上角分享
		 */
		onShareAppMessage: function() {},
		methods: {
			toDistCenter: function() {
				uni.showToast({
					icon: "none",
					title: '该功能未开源'
				});
			},
			toCouponCenter: function() {
				uni.showToast({
					icon: "none",
					title: '该功能未开源'
				});
			},
			toMyCouponPage: function() {
				uni.showToast({
					icon: "none",
					title: '该功能未开源'
				});
			},
			handleTips: function() {
				uni.showToast({
					icon: "none",
					title: '该功能未开源'
				});
			},
			toAddressList: function() {
				uni.navigateTo({
					url: '/pages/delivery-address/delivery-address'
				});
			},
			// 跳转绑定手机号
			toBindingPhone: function() {
				uni.navigateTo({
					url: '/pages/binding-phone/binding-phone'
				});
			},
			toOrderListPage: function(e) {
				var sts = e.currentTarget.dataset.sts;
				uni.navigateTo({
					url: '/pages/orderList/orderList?sts=' + sts
				});
			},

      /**
       * 查询用户信息
       */
      getUserInfo() {
        var ths = this;
        uni.showLoading();
        var params = {
          url: "/p/user/getUserInfoById",
          method: "GET",
          data: {},
          callBack: function(res) {
            uni.hideLoading();
            ths.setData({
              user: res
            });
          }
        };
        http.request(params);
      },


      /**
			 * 查询所有的收藏量
			 */
			showCollectionCount: function() {
				var ths = this;
				uni.showLoading();
				var params = {
					url: "/p/user/collection/count",
					method: "GET",
					data: {},
					callBack: function(res) {
						uni.hideLoading();
						ths.setData({
							collectionCount: res
						});
					}
				};
				http.request(params);
			},
      inputDialogToggle() {
        this.$refs.inputDialog.open()
      },
      recoveryOrder: function(orderNumber){
        uni.showLoading({
          mask: true
        });
        var params = {
          url: "/p/myOrder/recoveryOrder/"+orderNumber,
          method: "POST",
          data: {},
          callBack: res => {
            console.log("res",res)
            uni.hideLoading();
            if(res){
              this.getUserInfo();
              uni.showToast({
                title: "Reciclaje Exitoso",
                icon:"none"
              });
            }else{
              uni.showToast({
                title: "Fallo de Reciclaje!",
                icon:"none"
              })
            }
          }
        };
        http.request(params);

      },

			/**
			 * 我的收藏跳转
			 */
			myCollectionHandle: function() {
				var url = '/pages/prod-classify/prod-classify?sts=5';
				var id = 0;
				var title = "Favoritos";

				if (id) {
					url += "&tagid=" + id + "&title=" + title;
				}

				uni.navigateTo({
					url: url
				});
			},

			/**
			 * 去登陆
			 */
			toLogin: function() {
				uni.navigateTo({
					url: "../accountLogin/accountLogin"
				})
			},

			/**
			 * 退出登录
			 */
			logout: function() {
				// 请求退出登陆接口
				http.request({
					url: '/logOut',
					method: 'post',
					callBack: res => {
						util.removeTabBadge()

						uni.removeStorageSync('loginResult');
						uni.removeStorageSync('token');

						// this.$Router.pushTab('/pages/index/index')
						uni.showToast({
							title: "Cerrar Sesion",
							icon: "none"
						})

						this.setData({
							orderAmount: ''
						});
						setTimeout(() => {
							uni.switchTab({
								url: "/pages/index/index"
							})
						}, 1000)
					}
				})
			},
		}
	};
</script>
<style>
	@import "./user.css";
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
