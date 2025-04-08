import {User} from "../../user/models/user";
import {Campaign} from "../../campaigns/models/campaign";
import {Donor} from "../../donor/models/donor";

export class Donation {
  constructor(public id: number,
              public campaign_id: Campaign,
              public amount: number,
              public currency: string,
              public createdBy: User,
              public createdDate: Date,
              public benefactor: Donor,
              public approved: boolean,
              public approvedBy: User,
              public approveDate: Date,
              public notes: string) {
  }
}
