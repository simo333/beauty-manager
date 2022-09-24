import { Component } from '@angular/core';
import {AuthService} from "./_services/auth.service";
import {StorageService} from "./_services/storage.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'BeautyManagerApp';
  private roles: string[] = [];
  isLoggedIn = false;
  showAdminBoard = false;
  email?: string;

  constructor(private storageService: StorageService, private authService: AuthService) { }

  ngOnInit(): void {
    this.isLoggedIn = this.storageService.isLoggedIn();

    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.roles = user.roles;

      this.showAdminBoard = this.roles.includes('ROLE_ADMIN');
      this.email = user.email;
    }
  }

  public logout(): void {
    this.authService.logout().subscribe({
      next: res => {
        console.log(res);
      },
      error: err => {
        console.log(err);
      }
    });
    this.storageService.clean();
    console.log("Storages cleaned. User signed out.");
  }
}
