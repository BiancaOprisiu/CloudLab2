import {Permission} from "../../permissions/models/permission";

export class Role {
  constructor(public id: number,
              public name: string,
              public permisions: Permission[]
  ) {
  }
}
