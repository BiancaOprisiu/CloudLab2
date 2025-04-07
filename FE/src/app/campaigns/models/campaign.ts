import {Donor} from "../../donor/models/donor";
import {Donation} from "../../donations/models/donation";

export class Campaign {
  constructor(public id: number,
              public name: string,
              public purpose: string,
              public donors: Donor[]) {
  }
}
