export class AuthenticationRequest {

  constructor(data:Partial<AuthenticationRequest>) {
    Object.assign(this, data);
  }

  email?: string|null;
  password?: string|null;
  rememberMe?: boolean|null;

}

export class AuthenticationResponse {
  accessToken?: string;
  refreshToken?: string;
}

export class RegistrationRequest {

  constructor(data:Partial<RegistrationRequest>) {
    Object.assign(this, data);
  }

  email?: string|null;
  password?: string|null;

}
