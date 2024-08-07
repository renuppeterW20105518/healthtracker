<template id="registration-gym">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col">
            <h2>Gym Membership Form</h2>
            <p>Adults: There is an initial one-time enrollment fee of $150 and an annual subscription fee of $100.</p>
            <p>Children over 12 and under 18: There is an initial one-time enrollment fee of $60 and an annual
              subscription fee of $50.</p>
          </div>
        </div>
      </div>
      <div class="card-body">
        <form id="addTrainee">
          <div class="row">
            <div class="col-6">
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-first_name">First Name</span>
                </div>
                <input type="text" class="form-control" v-model="formData.firstName" name="firstName"
                       placeholder="First Name" required />
              </div>
            </div>
            <div class="col-6">
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-lastName">Last Name</span>
                </div>
                <input type="text" class="form-control" v-model="formData.lastName" name="lastName"
                       placeholder="Last Name"/>
              </div>
            </div>
          </div>

          <h6>Address</h6>
          <div class="row">
            <div class="col-6">
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-address">Address</span>
                </div>
                <input type="text" class="form-control" v-model="formData.address" name="address"
                       placeholder="Address"/>
              </div>
            </div>
            <div class="col-6">
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-eircode">Eircode</span>
                </div>
                <input type="text" class="form-control" v-model="formData.eircode" name="eircode"
                       placeholder="Eircode"/>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-6">
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-county">County</span>
                </div>
                <input type="text" class="form-control" v-model="formData.county" name="county" placeholder="County"/>
              </div>
            </div>

            <div class="col-6">
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-phoneNumber">Phone Number</span>
                </div>
                <input type="text" class="form-control" v-model="formData.phoneNumber" name="phoneNumber"
                       placeholder="Phone Number "/>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-6">
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-gender">Gender</span>
                </div>
                <select class="form-control" v-model="formData.gender" name="gender">
                  <option value="" disabled>Select Gender</option>
                  <option value="male">Male</option>
                  <option value="female">Female</option>
                  <option value="other">Other</option>
                </select>
              </div>
            </div>
            <div class="col-6">
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-dateOfBirth">Date of Birth</span>
                </div>
                <input type="date" class="form-control" v-model="formData.dateOfBirth" name="dateOfBirth">
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-6">
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-height">Height</span>
                </div>
                <input type="text" class="form-control" v-model="formData.height" name="height" placeholder="Height">
                <span class="input-group-text" id="input-cm">cm</span>
              </div>
            </div>
            <div class="col-6">
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-weight">Weight</span>
                </div>
                <input type="text" class="form-control" v-model="formData.weight" name="weight" placeholder="Weight">
                <span class="input-group-text" id="input-kg">kg</span>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-6">
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-paymentMethod">Payment Method</span>
                </div>
                <select class="form-control" v-model="formData.paymentMethod" name="paymentMethod">
                  <option value="" disabled>Select Payment Method</option>
                  <option value="visa">Visa</option>
                  <option value="mastercard">MasterCard</option>
                  <option value="bank_transfer">Bank Transfer</option>
                  <option value="google_pay">Google Pay</option>
                  <option value="revolut">Revolut</option>
                  <option value="cash">Cash</option>
                </select>
              </div>
            </div>
            <div class="col-6">
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-amount">Amount</span>
                </div>
                <input type="text" class="form-control" v-model="formData.amount" name="amount" placeholder="Amount">
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-6">
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-username">Username</span>
                </div>
                <input type="text" class="form-control" v-model="formData.username" name="username"
                       placeholder="Username">
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-6">
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-password">Password</span>
                </div>
                <input type="password" class="form-control" v-model="formData.password" name="password"
                       placeholder="Password">
              </div>
            </div>

            <div class="col-6">
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-confirmpassword">Confirm Password</span>
                </div>
                <input type="password" class="form-control" v-model="formData.confirmpassword" name="confirmpassword"
                       placeholder="Confirm Password">
              </div>
            </div>
          </div>

          <h2>Emergency Contact Information</h2>
          <div class="row">
            <div class="col-6">
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-emergency_contacts_first_name">First Name</span>
                </div>
                <input type="text" class="form-control" v-model="formData.emergency_contacts_first_name"
                       name="emergency_contacts_first_name" placeholder="Emergency Contact First Name">
              </div>
            </div>
            <div class="col-6">
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-emergency_contacts_last_name">Last Name</span>
                </div>
                <input type="text" class="form-control" v-model="formData.emergency_contacts_last_name"
                       name="emergency_contacts_last_name" placeholder="Emergency Contact Last Name">
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-6">
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-emergency_contacts_phone_number">Phone Number</span>
                </div>
                <input type="text" class="form-control" v-model="formData.emergency_contacts_phone_number"
                       name="emergency_contacts_phone_number" placeholder="Emergency Contact Phone">
              </div>
            </div>
            <div class="col-6">
              <div class="input-group mb-3">
                <div class="input-group-prepend">
                  <span class="input-group-text" id="input-emergency_contacts_relation">Relation</span>
                </div>
                <input type="text" class="form-control" v-model="formData.emergency_contacts_relation"
                       name="emergency_contacts_relation" placeholder="Emergency Contact Relation">
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col">
              <div v-if="errorMessage" class="alert alert-danger">{{ errorMessage }}</div>
              <button type="button" class="btn btn-info" @click="addTrainee">Register</button>
            </div>
          </div>

        </form>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("registration-gym", {
  template: "#registration-gym",
  data() {
    return {
      trainees: [],
      logins:[],
      emergencyContacts:[],
      formData: {
        firstName: "",
        lastName: "",
        address: "",
        eircode: "",
        county: "",
        phoneNumber: "",
        gender: "",
        dateOfBirth: "",
        height: "",
        weight: "",
        paymentMethod: "",
        amount: "",
        username: "",
        password: "",
        confirmPassword: "",
        emergency_contacts_first_name: "",
        emergency_contacts_last_name: "",
        emergency_contacts_phone_number: "",
        emergency_contacts_relation: "",
      },
      errorMessage: ''
    }
  },
  methods: {
    addTrainee: function () {
      if(!this.formData.firstName){
        this.errorMessage = 'First Name is required!';
      } else if(!this.formData.lastName){
        this.errorMessage = 'Last Name is required!';
      } else if(!this.formData.address){
        this.errorMessage = 'Address is required!';
      } else if(!this.formData.eircode){
        this.errorMessage = 'Eircode is required!';
      } else if(!this.formData.county){
        this.errorMessage = 'County is required!';
      } else if(!this.formData.phoneNumber){
        this.errorMessage = 'Phone Number is required!';
      } else if(!this.formData.gender){
        this.errorMessage = 'Gender is required!';
      } else if(!this.formData.dateOfBirth){
        this.errorMessage = 'Date Of Birth is required!';
      } else if(!this.formData.height){
        this.errorMessage = 'Height is required!';
      } else if(!this.formData.weight){
        this.errorMessage = 'Weight is required!';
      }  else if(!this.formData.paymentMethod){
        this.errorMessage = 'Payment Method is required!';
      } else if(!this.formData.amount){
        this.errorMessage = 'Amount is required!';
      } else if(!this.formData.username){
        this.errorMessage = 'Username is required!';
      } else if(!this.formData.password){
        this.errorMessage = 'Password is required!';
      } else if (this.formData.password !== this.formData.confirmpassword) {
        this.errorMessage = 'Passwords do not match!';
      } else if(!this.formData.emergency_contacts_first_name){
        this.errorMessage = 'Emergency contact first name is required!';
      } else if(!this.formData.emergency_contacts_last_name){
        this.errorMessage = 'Emergency contact last name is required!';
      } else if(!this.formData.emergency_contacts_phone_number){
        this.errorMessage = 'Emergency contact phone is required!';
      } else if(!this.formData.emergency_contacts_relation){
        this.errorMessage = 'Emergency contact relation is required!';
      } else {
        this.errorMessage = '';
        alert('Passwords match. Form submitted!');
      }

      const url = `/api/registration-gym`

      axios.post(url,
          {
            firstName: this.formData.firstName,
            lastName: this.formData.lastName,
            address: this.formData.address,
            eircode: this.formData.eircode,
            county: this.formData.county,
            phoneNumber: this.formData.phoneNumber,
            gender: this.formData.gender,
            dateOfBirth: this.formData.dateOfBirth,
            height: this.formData.height,
            weight: this.formData.weight,
            paymentMethod: this.formData.paymentMethod,
            amount: this.formData.amount,
            username: this.formData.username,
            password: this.formData.password,
            emergency_contacts_first_name: this.formData.emergency_contacts_first_name,
            emergency_contacts_last_name: this.formData.emergency_contacts_last_name,
            emergency_contacts_phone_number: this.formData.emergency_contacts_phone_number,
            emergency_contacts_relation: this.formData.emergency_contacts_relation
          })
          .then(response => {
            this.trainees.push(response.data)
          })
          .catch(error => {
            console.log(error)
          })
    }
  }
});
</script>