import {Notification} from "../../notifications/models/notification";

export class LoginResponse{
  constructor(
    public accessToken: string,
    public id: number,
    public username: string,
    public email: string,
    public roles: string[],
    public loginCount: number,
    public passwordChangeRequired: boolean,
    public notifications: string[]
  ) {
  }
}
