import { Routes } from '@angular/router';
import { DashboardAdminComponent } from './pages/admin/dashboard-admin/dashboard-admin.component';
import { ManagerAccountComponent } from './pages/admin/manager-account/manager-account.component';
import { AgentComponent } from './pages/agents/agent/agent.component';
import { DashboardComponent } from './pages/agents/dashboard/dashboard.component';
import { MainComponent as CustomerMainComponent, MainComponent } from './pages/customer/main/main.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { AuthGuard } from './auth.guard';
import { ShopComponent } from './pages/agents/shop/shop.component';
import { HairstylistComponent } from './pages/agents/hairstylist/hairstylist.component';
import { CategoryComponent } from './pages/agents/category/category.component';
import { ProductComponent } from './pages/agents/product/product.component';
import { ServiceComponent } from './pages/agents/service/service.component';
import { CustomerInforComponent } from './pages/customer/customer-infor/customer-infor.component';
import { PreferenceComponent } from './pages/customer/preference/preference.component';
import { CustomerServiceComponent} from './pages/customer/customer-service/customer-service.component';
import { BookingComponent} from './pages/customer/booking/booking.component';
import { CustomerProductComponent} from './pages/customer/customer-product/customer-product.component';
import { CustomerLayoutComponent } from './layouts/customer-layout/customer-layout.component';
import { CartComponent } from './pages/customer/cart/cart.component';
import { FakeBankComponent } from './pages/customer/fake-bank/fake-bank.component';
import { VnpayReturnComponent } from './pages/vnpay-return/vnpay-return.component';
import {  BookingPaymentComponent } from './pages/customer/booking-payment/booking-payment.component';
import { ProductDetailComponent } from './pages/customer/product-detail/product-detail.component';
import { BookingAgentComponent } from './pages/agents/agent-booking/agent-booking.component';
import { RevenueAnalysisComponent } from './pages/agents/revenue-analysis/revenue-analysis.component';
import { AgentPackageComponent } from './pages/agent-package/agent-package.component';
import { OrderManagementComponent } from './pages/agents/order-management/order-management.component';
import { ManagerPackageComponent } from './pages/admin/manager-package/manager-package.component';
import { MasterDataComponent } from './pages/admin/master-data/master-data.component';





export const routes: Routes = [ 
  { path: '', redirectTo: '/customer/main', pathMatch: 'full' }, 

  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

    // admin
    {
      path: 'admin',
      component: DashboardAdminComponent, 
      canActivate: [AuthGuard],
      data: { roles: ['ADMIN'] },
      children: [
        { path: '', redirectTo: 'manager-account', pathMatch: 'full' },  
        { path: 'manager-account', component: ManagerAccountComponent },
        { path: 'manager-package', component: ManagerPackageComponent},
        { path: 'dashboard', component: MasterDataComponent}
      ]
    },
    
  {
    path: 'agents/select-package',
    component: AgentPackageComponent,
    canActivate: [AuthGuard],
    data: { roles: ['AGENT'] }
  },
    //agent
    {
      path: 'agents',
      children: [
        {
          path: 'dashboard',
          component: DashboardComponent,
          canActivate: [AuthGuard],
          data: { roles: ['AGENT'] },
          children: [
            { path: 'agent', component: AgentComponent },
            {path: 'shop', component: ShopComponent},
            { path: 'hairstylist', component:HairstylistComponent },
            { path: 'category', component: CategoryComponent },
            { path: 'product', component: ProductComponent},
            { path: 'service', component: ServiceComponent},
            { path: 'booking', component: BookingAgentComponent},
            { path: 'order', component: OrderManagementComponent},
            { path: 'analyst', component: RevenueAnalysisComponent},
            { path: '', redirectTo: 'agent', pathMatch: 'full' }
          ]
        }
      ]
    },
    
    { path: 'fake-bank', component: FakeBankComponent },
    { path: 'vnpay-return', component: VnpayReturnComponent },
  //customer
  {
    path: '',
    component: CustomerLayoutComponent,
    canActivate: [AuthGuard],
    data: { roles: ['CUSTOMER'] },
    children: [
      { path: '', component: MainComponent },
      { path: 'customer/main', component: CustomerMainComponent },
      { path: 'customer/infor', component: CustomerInforComponent },
      { path: 'customer/preference', component: PreferenceComponent },
      { path: 'customer/service', component: CustomerServiceComponent},
      { path: 'customer/booking', component: BookingComponent},
      { path: 'customer/product', component: CustomerProductComponent},
      { path: 'customer/cart', component: CartComponent },
      { path: 'customer/payment/booking/:bookingId', component: BookingPaymentComponent },
      { path: 'customer/payment/order/:orderId', component: BookingPaymentComponent },
      { path: 'product/:id', component: ProductDetailComponent }

    ]
  }
];
