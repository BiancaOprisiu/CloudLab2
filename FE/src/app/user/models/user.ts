import {Role} from "../../roles/models/role";
import {Campaign} from "../../campaigns/models/campaign";
import {Donation} from "../../donations/models/donation";
import {Notification} from "../../notifications/models/notification";

export class User {
  constructor(public id: number,
              public firstname: string,
              public lastname: string,
              public username: string,
              public email: string,
              public password: string,
              public mobilenumber: string,
              public active: boolean,
              public consecutiveUnsuccessfulAttempts: number,
              public loginCount: number,
              public passwordChangeRequired: boolean,
              public roles: Role[],
              public campaigns: Campaign[],
              public notifications: Notification[],
              public createdDonations: Donation[],
              public approvedDonation: Donation[]
  ) {
  }
}
