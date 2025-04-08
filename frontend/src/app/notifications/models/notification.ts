import {User} from "../../user/models/user";

export class Notification {
  constructor(public id: number,
              public message: string,
              public createdDate: Date,
              public user: User,
              public seen: boolean,
              public title: string
              ) {
  }
}
