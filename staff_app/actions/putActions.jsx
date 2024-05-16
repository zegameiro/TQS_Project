export const editFacility = async (axios, facilityData) => {
  return axios.put("facility/admin/edit", facilityData)
}