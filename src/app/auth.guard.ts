import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router } from '@angular/router';
import { AuthService } from './services/auth.service';
import { JwtHelperService } from './services/jwthelper.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(
    private authService: AuthService,
    private router: Router,
    private jwtHelper: JwtHelperService
  ) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const token = this.authService.getToken();
    console.log('✅ AuthGuard is running');
    console.log('Token:', token);

    if (!token || this.jwtHelper.isTokenExpired(token)) {
      alert('You are not logged in!');
      console.log('❌ Token is invalid or expired');
      this.router.navigate(['/login']);
      return false;
    }

    const decodedToken = this.jwtHelper.decodeToken(token);
    const userRole = decodedToken?.role;
    const accountStatus = decodedToken?.status; 

    const requiredRoles = route.data['roles'];
    console.log('✅ Decoded token:', decodedToken);
    console.log('🔐 User role:', userRole);
    console.log('🔒 Required roles:', requiredRoles);

    
    if (!requiredRoles || !userRole || !requiredRoles.includes(userRole)) {
      alert('You do not have access!');
      this.router.navigate(['/login']);
      return false;
    }

    if (userRole === 'AGENT' && accountStatus !== 'ACTIVE' && route.routeConfig?.path !== 'agents/select-package') {
      alert('You need to select a package to continue!');
      this.router.navigate(['/agents/select-package']);
      return false;
    }

    return true;
  }
}
