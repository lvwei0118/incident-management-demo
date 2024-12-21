import moment from "moment";

export const dataValidate = (values) => {
    let data = values;
    // console.log(data);
    data.code = data.code.toString();
    data.name = data.name.toString();
    data.type = data.type.toString();
    data.description = data.description.toString();
    return data;
}

export const updDataValidate = values => {
    let data = values;
    data.code = data.code.toString();
    data.name = data.name.toString();
    data.type = data.type.toString();
    data.description = data.description.toString();
    data.isCache = data.isCache.toString();
    return data;
}
