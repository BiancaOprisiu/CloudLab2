import {Campaign} from "../../campaigns/models/campaign";
import {Donation} from "../../donations/models/donation";

export class Donor {
  constructor(public id: number,
              public firstname: string,
              public lastname: string,
              public additionalName: string,
              public maidenName: string,
              public campaigns: Campaign[]) {
  }
}
