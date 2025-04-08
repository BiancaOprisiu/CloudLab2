import {Campaign} from "../../campaigns/models/campaign";
import {User} from "../../user/models/user";
import {Donor} from "../../donor/models/donor";


export class DonationDTO {
  public constructor(init?: Partial<DonationDTO>) {
    Object.assign(this, init);
  }


}
