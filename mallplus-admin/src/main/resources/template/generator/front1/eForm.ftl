  @import '../../../../components/templatePage/static/common/common.less';

  .container {
    position: relative;
  }

  //.container .redu-box {
  //  padding: 0 20px;
  //}

  .container .redu-bread {
    line-height: 30px;
    height: 30px;
  }

  .container .redu-content {
    padding: 20px 20px 100px 20px;
  }

  .container .redu-inset {
    background: #fff;
  }

  .container .star {
    color: red;
    padding: 5px;
    font-size: 20px;
    margin-right: 10px;
  }

  .container .redu-title {
    height: 30px;
    line-height: 30px;
    color: #000;
    padding-left: 15px;
    background-color: #f2f2f2;
  }

  .container .inset-content {
    padding: 40px 0 30px 50px;
    background: #fff;
  }

  .container .high-opation {
    padding: 10px;
    cursor: pointer;
    color:#0083B0
  }

  .container .set {
    width: 600px;
    padding: 20px 20px 20px 10px;
    margin-bottom: 10px;
    border: 1px dashed #aeaeae;
    background: #F2F2F2;
    position: relative;
  }

  .container .set .delete {
    position: absolute;
    height: 30px;
    width: 30px;
    top: -15px;
    right: -18px;
    font-size: 20px;
    cursor: pointer;
  }

  .container .formBtn {
    width: 150px;
    margin: 0 auto;
    margin-top: 30px;
    text-align: center;
  }

  .container .comBtn {
    margin: 0 5px;
  }

  .container .mask {
    width: 100%;
    height: 100vh;
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.4);
    z-index: 99;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }

  .container .set .delete {
    position: absolute;
    height: 30px;
    width: 30px;
    top: -15px;
    right: -18px;
    font-size: 20px;
    cursor: pointer;
  }

  .container .time-set {
    width: 700px;
    height: 270px;
    border: 1px solid #aeaeae;
    background: #fff;
  }

  .container .set-title {
    padding: 8px;
    font-size: 14px;
    background: @mainColor;
    color: #fff;
    position: relative;
  }

  .container .close {
    position: absolute;
    right: 0;
    top: 0;
    padding: 8px 10px;
    text-align: center;
    background: rgba(0, 0, 0, 0.4);
    cursor: pointer;
  }

  .container .choose-week {
    border-bottom: 1px solid #aeaeae;
    padding: 30px 0 0 0;
  }

  .container .week-btn {
    width: 70px;
    margin-right: 10px;
  }

  .limitLength {
    position: absolute;
    margin-left: 10px;
    color: #bbb;
  }

  .bgColor {
    background: @mainColor;
    color: #fff;
  }
