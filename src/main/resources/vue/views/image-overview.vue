<template id="image-overview">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">
            Images
          </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Add"
                    class="btn btn-info btn-simple btn-link"
                    @click="hideForm = !hideForm">
              <i class="fa fa-plus" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body" :class="{ 'd-none': hideForm}">
        <form id="addImage">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-image-title">Title</span>
            </div>
            <input type="text" class="form-control" v-model="formData.title" name="title" placeholder="Title"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-image-description">Description</span>
            </div>
            <input type="text" class="form-control" v-model="formData.description" name="description" placeholder="Description"/>
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-image-file-path">File</span>
            </div>
            <input type="file" class="form-control" @change="onFileChange" v-model="formData.image_file_path">
          </div>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-userId-started">User Id</span>
            </div>
            <input type="number" class="form-control" v-model="formData.userId" name="userId" placeholder="User Id"/>
          </div>
        </form>
        <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addImage()">Add Image</button>
      </div>
    </div>
    <table class="table table-bordered table-striped table-hover">
      <thead>
      <tr>
        <th scope="col" class="text-center">Title</th>
        <th scope="col" class="text-center">Description</th>
        <th scope="col" class="text-center">Image</th>
        <th scope="col" class="text-center">Actions</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(image, index) in paginatedImages" :key="index">
        <td class="text-center">{{ image.title }}</td>
        <td class="text-center">{{ image.description }}</td>
        <td class="text-center">{{ image.image_file_path }}</td>
        <td class="text-center">
          <a :href="`/images/${image.id}`">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link mx-1">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                    @click="deleteImage(image, index)">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </a>
        </td>
      </tr>
      </tbody>
    </table>

    <nav aria-label="Image Pagination">
      <ul class="pagination justify-content-center mt-3">
        <li class="page-item" :class="{ 'disabled': currentPage === 1 }">
          <a class="page-link" href="#" aria-label="Previous" @click="prevPage">
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
        <li class="page-item" v-for="page in totalPages" :key="page" :class="{ 'active': currentPage === page }">
          <a class="page-link" href="#" @click="setPage(page)">{{ page }}</a>
        </li>
        <li class="page-item" :class="{ 'disabled': currentPage === totalPages }">
          <a class="page-link" href="#" aria-label="Next" @click="nextPage">
            <span aria-hidden="true">&raquo;</span>
          </a>
        </li>
      </ul>
    </nav>
  </app-layout>
</template>

<script>
app.component("image-overview", {
  template: "#image-overview",
  data() {
    return {
      images: [],
      formData: { title: "",description: "", image_file_path: "", userId: "" },
      currentPage: 1,
      itemsPerPage: 5,
      hideForm: true
    };
  },
  created() {
    this.fetchImages();
  },
  computed: {
    paginatedImages() {
      const startIndex = (this.currentPage - 1) * this.itemsPerPage;
      const endIndex = startIndex + this.itemsPerPage;
      return this.images.slice(startIndex, endIndex);
    },
    totalPages() {
      return Math.ceil(this.images.length / this.itemsPerPage);
    },
  },
  methods: {
    async fetchImages() {
      try {
        const response = await axios.get("api/images");
        this.images = response.data;
      } catch (error) {
        alert("Error while fetching images");
      }
    },
    addImage: function() {
      const url = `/api/images`;
      axios.post(url,
          {
            title: this.formData.title,
            description: this.formData.description,
            image_file_path: this.formData.image_file_path,
            userId: this.formData.userId
          })
          .then(response => {
            this.images.push(response.data)
            this.hideForm= true;
          })
          .catch(error => {
            console.log(error)
          })
    },
    deleteImage: function (image, index) {
      if (confirm('Are you sure you want to delete this image? This action cannot be undone.')) {
        const imageId = image.id;
        const url = `/api/images/${imageId}`;
        axios.delete(url)
            .then(response =>
                this.users.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    setPage(page) {
      this.currentPage = page;
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
      }
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
      }
    },
  }
});
</script>