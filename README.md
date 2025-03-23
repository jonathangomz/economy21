# Economy21

The Economy21 API gets his name after the spreadsheet that I use for my
personal finances. I started working on that spreadsheet since 21, and I
named like that to not confuse myself with other financial spreadsheets
from other years. But this file evolve into the one that I'm still using
even 4 years later. But now I need more and from this necessity this project
was born. 

## Endpoints
### Users
🚧 Working on it... maybe use a provider

### Accounts
- [ ] Each account has an owner (the user).
- [ ] An owner can have multiple accounts.
- [X] An account can be of type debit or credit.
- [X] When an account is created with an initial amount. An initial movement is created with that amount.
- [ ] The total of the account cannot be updated directly because is updated from the movements.
- [ ] When an account is soft deleted is more like an archive
- [ ] When an account is deleted the movements are also deleted.
- [ ] If an account is credit
  - [ ] Need to save the credit information like cutoff day or credit limit.
  - [ ] Need to calculate the previous, current and next cutoff and payment limit date
  - [ ] Need to calculate the monthly debt

| STATUS | VERB     | PATH                        | Description                                       |
|--------|----------|-----------------------------|---------------------------------------------------|
| 🚧     | `GET`    | `/api/accounts`             | Get the accounts list                             |
| 🚧     | `POST`   | `/api/accounts`             | Create a new account                              |
| 🚧     | `GET`    | `/api/accounts/{accountId}` | Get a single account                              |
| 🚧     | `PUT`    | `/api/accounts/{accountId}` | Updates an account                                |
| 🚧     | `DELETE` | `/api/accounts/{accountId}` | Delete an account. Can be a normal or soft delete |

### Movements
- [X] When a new movement is created, the total of the account is updated.
- [X] When a movement is deleted, the total of the account is updated.
- [ ] When a movement total is updated, the total of the account is updated
- [ ] If the movements are for a credit account, then they can have extra information like: msi, settled, linked_movement (for a payment to a msi)
- [ ] The movements marked as settled should not count for the account debt
- [ ] The movements marked as msi should not count for the account debt and should have a list of monthly payments, and the current month should count for the account debt

| Status | VERB     | PATH                                                        | Description               |
|--------|----------|-------------------------------------------------------------|---------------------------|
| 🚧     | `GET`    | `/api/accounts/{accountId}/movements`                       | Get the account movements |
| 🚧     | `POST`   | `/api/accounts/{accountId}/movements`                       | Create a new movement     |
| 🚧     | `GET`    | `/api/accounts/{accountId}/movements/{movementId}`          | Get a single movement     |
| 🚧     | `PUT`    | `/api/accounts/{accountId}/movements/{movementId}`          | Updates a movement        |
| 🚧     | `DELETE` | `/api/accounts/{accountId}/movements/{movementId}`          | Delete a movement.        |
| 🚧     | `GET`    | `/api/accounts/{accountId}/movements/{movementId}/deferred` | Get the monthly amounts   |

### Services
- [ ] A service should always be getting the next payment date
- [ ] A service can confirm the current payment to generate a new movement on the linked account
- [ ] A service can be linked to an account, but not necessarily because it can be paid with cash for example

| Status | VERB     | PATH                                | Description                                                               |
|--------|----------|-------------------------------------|---------------------------------------------------------------------------|
| 🚧     | `GET`    | `/api/services`                     | Get the services                                                          |
| 🚧     | `POST`   | `/api/services`                     | Create a new service                                                      |
| 🚧     | `GET`    | `/api/services/{serviceId}`         | Get a single service                                                      |
| 🚧     | `PUT`    | `/api/services/{serviceId}`         | Updates a service                                                         |
| 🚧     | `PATCH`  | `/api/services/{serviceId}/confirm` | Create a new movement on the linked account using the service information |
| 🚧     | `DELETE` | `/api/services/{serviceId}`         | Delete a service.                                                         |