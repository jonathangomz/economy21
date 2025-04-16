# Accounts requirements

## Index
1. [Get all](#get-all)
2. [Get single](#get-single)
3. [Create](#create)
4. [Update](#update)
5. [Update credit information](#update-credit-information)
6. [Inactive](#inactivate)
7. [Delete](#delete)

## Get all
> 🧭️ `GET: /api/accounts`
 
### Description
Retrieves all the user accounts.

### Responses

| Code | Description | Returns           |
|------|-------------|-------------------|
| 200  | Ok          | An `Account` list |

## Get single
> 🧭️ `GET: /api/accounts/{accountId}`

### Description
Retrieves a single user account.

### Responses

| Code | Description                                                 | Returns              |
|------|-------------------------------------------------------------|----------------------|
| 200  | Ok                                                          | An `Account`         |
| 404  | No account {accountId} was found for the authenticated user | An `NotFoundContent` |


## Create
> 🧭️ `POST: /api/accounts`

### Description
Create a new account.

> ⚠️ You cannot update the `type` of an account.

#### Debit
The debit accounts will be initialized with a single movement
to symbolize all the previous movements until that point,
unless the `total` is not provided or is provided as `0`, in
that case the initial `total` is `0` and no initial movement
is created.

Generally you shouldn't update manually the `total` of the
account, that's why we do not allow to do that, but you can
update the account `total` indirectly by updating the initial
movement to recalculate the `total`.

#### Credit
The credit accounts are more complex. The `total` is going to
be used for the initial `total` and as the `creditLimit`.
When you add movements to a credit account the `total` is updated
but the `creditLimit` stay the same.

The `total` of a credit account is how much credit you have left
to use so is directly linked to the `creditLimit`. You cannot update
the `total` manually, but you can update the `total` of a credit
account by modifying the `creditLimit`. More information about this
can be found on the [creditLimit](#creditlimit) section inside the
[description](#description-2) of the [Update credit information](#update-credit-information) section.

### Fields and Validations
- `name`: `string`
  - required
  - max `100` characters
- `type`: `string`
  - required
  - only accepts `DEBIT` or `CREDIT`

For `CREDIT` accounts only:
- `total`: `decimal`
  - required
  - only accepts positive numbers
- `cutoffDay`: `integer`
    - required
    - only accepts values between `1` and `31`
- `intervalPaymentLimit`: `integer`
    - optional
    - only accepts values between `1` and `30`
    - default value of `20`

For `DEBIT` accounts only:
- `total`: `decimal`
  - optional
  - only accepts positive numbers
  - default value of `0`

#### Notes
If type is `DEBIT`:
- `cutoffDay` and `intervalPaymentLimit` are ignored

If type is `CREDIT`:
- `total` is used for both `total` and as the `creditLimit`
- `cutoffDay` and `intervalPaymentLimit` are used

---

## Update
> 🧭 `PUT: /api/accounts/{accountId}`

### Description
Update the account information. But only the `name` can
be updated. If you want to update the credit information
go to [Update credit information](#update-credit-information)
section.

### Fields and Validations
- `name`: `string`
  - optional 
  - max `100` characters

---

## Update credit information
> 🧭 `PUT: /api/accounts/{accountId}/credit`

### Description
Update the account credit information.

> ⚠️ If we detect that the banks do not offer the option to
> update the `intervalPaymentLimit` we will delete also that
> option 

We use a different endpoint because the updates to the 
credit information needs specific validations and trigger
some recalculations.

#### `creditLimit`
If the `creditLimit` is updated the total needs to be
recalculated, so it will trigger and update for the `total`.
But have in mind that you cannot update the `creditLimit`
to a value less than the current `total` for obvious reasons.

The flow should be something like this: you create the account
with an initial `total`. The `total` and `creditLimit` amounts are
initialize with the same value, and it means that you have at your
disposal the entire of your `creditLimit`. If you add a charge
movement the `total` will decrease, but the `creditLimit` will
stay the same. If you update the `creditLimit` the `newCreditLimit`
should not be less than the current `total`, then the difference is
calculated with `newCreditLimit - creditLimit` and that difference
is applied to `total`. The difference can be negative or positive.
If it is negative the `total` will decrease and if it is positive will
increment.

For example:
- The `total` and `creditLimit` are `1000`
- A charge movement is added with an amount of `100`
- The `total` is decreased to `900`
- A charge movement is added with an amount of `200`
- The total is decreased to `700`
- If you try to update the `creditLimit` value to `699` or less an error will occur
- If you try to update the `creditLimit` value to `700`
  - the difference between the new and the current `creditLimit` is calculated, ex. `700 - 1000 = -300`.
  - the difference is applied to the current `total`, ex. `700 + (-300) = 400`
  - the new `total` should be `400` and the new `creditLimit` should be `700`
  - the debt still is `300`, and the rule still applies: the difference between the `creditLimit` and `total` is the total debt.
- If you try to update the `creditLimit` value to `1500`:
  - the difference between the new and the current `creditLimit` is calculated, ex. `1500 - 1000 = 500`.
  - the difference is applied to the current `total`, ex. `700 + (+500) = 1200`
  - the new `total` should be `1200` and the new `creditLimit` should be `1500`
  - the debt still is `300`, and the rule still applies: the difference between the `creditLimit` and `total` is the total debt.

#### `cutoffDay & intervalPaymentLimit`
The `cutoffDay` and the `intervalPaymentLimit` updates are
the more complex updates. When any or both of these fields
are updated the `currentDebt` needs to be recalculated using a
recalculated cutoff date, and a recalculated payment limit
date. The previous cutoff date stay the same and that period can
have more days than usual as a transition period. 
Also, for the `cutoffDay`, the deferred movements dates should
be recalculated for the movements that still has not being paid.

Our logic to update the `cutoffDay` is as follows: if the `newCutoffDay` is
before or equal to the current day of month, the next cutoff date will
be on the next month, if is after the next cutoff date should be on
the same month on that day.

For example:
1. The `cutoffDay` is `18`
2. The current date is `14-04-2024` (April's 14)
3. The current `cutoffDate` should be `18-04-2024`
4. If the `newCutoffDay` is `10` (before today's day `14`)
   1. The new `cutoffDate` will be `10-05-2024`
   2. The transition period will have `54` days, from `18-03-2024` to `10-05-2025`
5. If the `newCutoffDay` is `14` (equal to today's day `14`)
   1. The new `cutoffDate` will be `14-05-2024`
   2. The transition period will have `58` days, from `18-03-2024` to `14-05-2025`
6. If the `newCutoffDay` is `25` (after today's day `14`)
   1. The new `cutoffDate` will be `25-04-2024`
   2. The transition period will have `39` days, from `18-03-2024` to `25-04-2025`
7. The `paymentLimitDate` is updated according to the new `cutoffDate` and the `intervalPaymentLimit`

> ⚠️ This logic can be different on your bank so you if you have 
> detected a different logic please contact us so we can implement
> that logic.

When you update the `cutoffDay` or `intervalPaymentLimit` only
the current period is affected by these operations. The periods
before are maintained the same and the followings are applied with
the new information.

When you finish a period (you make a payment that cover the monthly debt)
you need to do a period verification. After that verification, you need to
mark the period as completed and then all the movements inside that period
are going to be set as immutable. You have until the payment limit date to
make any 

> 💡 The period verification is done so you can compare with the bank
> information to confirm that the amounts are equal.

### Fields and Validations
- `creditLimit`: `decimal`
  - optional
- `cutoffDay`: `integer`
  - optional
  - only accepts values between `1` and `31`
- `intervalPaymentLimit`: `integer`
  - optional
  - only accepts values between `1` and `30`

---

## Inactivate
> 🧭 `PATCH: /api/accounts/{accountId}/status?active={true|false}`

### Description
An account can be marked as inactive setting his field `active` to `false`.

### Fields and Validations
- The `active` field required on the query parameters with a `boolean` value

## Delete
> 🧭️ `DELETE: /api/accounts/{accountId}`

### Description
Delete a user account. Use it soft delete so the account it's not really
deleted, just marked as deleted.