# 0.学习目标

- 独立实现品牌新增
- 实现图片上传
- 了解FastDFS的安装
- 使用FastDFS客户端实现上传



# 1.品牌的新增

昨天我们完成了品牌的查询，接下来就是新增功能。

## 1.1.页面实现

### 1.1.1.初步编写弹窗

当我们点击新增按钮，应该出现一个弹窗，然后在弹窗中出现一个表格，我们就可以填写品牌信息了。

我们查看Vuetify官网，弹窗是如何实现：

![1526115791468](assets/1526115791468.png)

另外，我们可以通过文档看到对话框的一些属性：

- value：控制窗口的可见性，true可见，false，不可见
- max-width：控制对话框最大宽度
- scrollable ：是否可滚动，要配合v-card来使用，默认是false
- persistent ：点击弹窗以外的地方不会关闭弹窗，默认是false

现在，我们来使用一下。

首先，我们在data中定义一个show属性，来控制对话框的显示状态：

 ![1526116451280](assets/1526116451280.png)

然后，在页面添加一个`v-dialog`

```html
<!--弹出的对话框-->
<v-dialog max-width="500" v-model="show" persistent>
    <v-card>
        <!--对话框的标题-->
        <v-toolbar dense dark color="primary">
            <v-toolbar-title>新增品牌</v-toolbar-title>
        </v-toolbar>
        <!--对话框的内容，表单-->
        <v-card-text class="px-5">
            我是表单
        </v-card-text>
    </v-card>
</v-dialog>
```

说明：

- 我们给dialog指定了3个属性，分别是

  - max-width：限制宽度
  - v-model：value值双向绑定到show变量，用来控制窗口显示
  - persisitent：控制窗口不会被意外关闭

- 因为可滚动需要配合`v-card`使用，因此我们在对话框中加入了一个`v-card`

  - 在`v-card`的头部添加了一个 `v-toolbar`，作为窗口的头部，并且写了标题为：新增品牌
    - dense：紧凑显示
    - dark：黑暗主题
    - color：颜色，primary就是整个网站的主色调，蓝色
  - 在`v-card`的内容部分，暂时空置，等会写表单

- `class=“px-5"`：vuetify的内置样式，含义是padding的x轴设置为5，这样表单内容会缩进一些，而不是顶着边框

  基本语法：`{property}{direction}-{size}`

  - property：属性，有两种`padding`和`margin`
    - `p`：对应`padding`
    - `m`：对应`margin`
  - direction：只padding和margin的作用方向，
    - `t` - 对应`margin-top`或者`padding-top`属性
    - `b` - 对应`margin-bottom` or `padding-bottom`
    - `l` - 对应`margin-left` or `padding-left`
    - `r` - 对应`margin-right` or `padding-right`
    - `x` - 同时对应`*-left`和`*-right`属性
    - `y` - 同时对应`*-top`和`*-bottom`属性
  - size：控制空间大小，基于`$spacer`进行倍增，`$spacer`默认是16px
    - `0`：将`margin`或padding的大小设置为0
    - `1` - 将`margin`或者`padding`属性设置为`$spacer * .25`
    - `2` - 将`margin`或者`padding`属性设置为`$spacer * .5`
    - `3` - 将`margin`或者`padding`属性设置为`$spacer`
    - `4` - 将`margin`或者`padding`属性设置为`$spacer * 1.5`
    - `5` - 将`margin`或者`padding`属性设置为`$spacer * 3`

### 1.1.2.实现弹窗的可见和关闭

> 窗口可见

接下来，我们要在点击新增品牌按钮时，将窗口显示，因此要给新增按钮绑定事件。

```js
<v-btn color="primary" @click="addBrand">新增品牌</v-btn>
```

然后定义一个addBrand方法：

```js
addBrand(){
    // 控制弹窗可见：
    this.show = true;
}
```

效果：

![1526118714621](assets/1526118714621.png)



> 窗口关闭

现在，悲剧发生了，因为我们设置了persistent属性，窗口无法被关闭了。除非把show属性设置为false

因此我们需要给窗口添加一个关闭按钮：

```html
<!--对话框的标题-->
<v-toolbar dense dark color="primary">
    <v-toolbar-title>新增品牌</v-toolbar-title>
    <v-spacer/>
    <!--关闭窗口的按钮-->
    <v-btn icon @click="closeWindow"><v-icon>close</v-icon></v-btn>
</v-toolbar>
```

并且，我们还给按钮绑定了点击事件，回调函数为closeWindow。

接下来，编写closeWindow函数：

```js
closeWindow(){
    // 关闭窗口
    this.show = false;
}
```



效果：

 ![1526119096686](assets/1526119096686.png)



### 1.1.3.新增品牌的表单页

接下来就是写表单了。我们有两种选择：

- 直接在dialog对话框中编写表单代码
- 另外编写一个组件，组件内写表单代码。然后在对话框引用组件

选第几种？



我们选第二种方案，优点：

- 表单代码独立组件，可拔插，方便后期的维护。
- 代码分离，可读性更好。



我们新建一个`MyBrandForm.vue`组件：

 ![1526119788914](assets/1526119788914.png)

将MyBrandForm引入到MyBrand中，这里使用局部组件的语法：

先导入自定义组件：

```js
  // 导入自定义的表单组件
  import MyBrandForm from './MyBrandForm'
```

然后通过components属性来指定局部组件：

```js
components:{
    MyBrandForm
}
```

然后在页面中引用：



页面效果：

 ![1526128384960](assets/1526128384960.png)

### 1.1.4.编写表单

#### 1.1.4.1.表单

查看文档，找到关于表单的部分：

![1526128476264](assets/1526128476264.png)

`v-form`，表单组件，内部可以有许多输入项。`v-form`有下面的属性：

- value：true，代表表单验证通过；false，代表表单验证失败

`v-form`提供了两个方法：

- reset：重置表单数据
- validate：校验整个表单数据，前提是你写好了校验规则。返回Boolean表示校验成功或失败



我们在data中定义一个valid属性，跟表单的value进行双向绑定，观察表单是否通过校验，同时把等会要跟表单关联的品牌brand对象声明出来：

```js
  export default {
    name: "my-brand-form",
    data() {
      return {
        valid:false, // 表单校验结果标记
        brand:{
          name:'', // 品牌名称
          letter:'', // 品牌首字母
          image:'',// 品牌logo
          categories:[], // 品牌所属的商品分类数组
        }
      }
    }
  }
```

然后，在页面先写一个表单：

```html
<v-form v-model="valid">

</v-form>
```



#### 1.1.4.2.文本框

我们的品牌总共需要这些字段：

- 名称
- 首字母
- 商品分类，有很多个
- LOGO

表单项主要包括文本框、密码框、多选框、单选框、文本域、下拉选框、文件上传等。思考下我们的品牌需要哪些？

- 文本框：品牌名称、品牌首字母都属于文本框
- 文件上传：品牌需要图片，这个是文件上传框
- 下拉选框：商品分类提前已经定义好，这里需要通过下拉选框展示，提供给用户选择。

先看文本框，昨天已经用过的，叫做`v-text-field`：

 ![1526129519056](assets/1526129519056.png)

查看文档，`v-text-field`有以下关键属性：

- **append-icon**：文本框后追加图标，需要填写图标名称。无默认值
- clearable：是否添加一个清空图标，点击会清空文本框。默认是false
- color：颜色
- counter：是否添加一个文本计数器，在角落显示文本长度，指定true或允许的组大长度。无默认值
- dark：是否应用黑暗色调，默认是false
- disable：是否禁用，默认是false
- flat：是否移除默认的动画效果，默认是false
- full-width：指定宽度为全屏，默认是false
- hide-details：是否因此错误提示，默认是false
- hint：输入框的提示文本
- **label**：输入框的标签
- **multi-line**：是否转为文本域，默认是false。文本框和文本域可以自由切换
- placeholder：输入框占位符文本，focus后消失
- **required**：是否为必填项，如果是，会在label后加*，不具备校验功能。默认是false
- **rows**：文本域的行数，`multi-line`为true时才有效
- **rules**：指定校验规则及错误提示信息，数组结构。默认[]
- **single-line**：是否单行文本显示，默认是false
- **suffix**：显示后缀

接下来，我们先添加两个字段：品牌名称、品牌的首字母，校验规则暂时不写：

```html
  <v-form v-model="valid">
    <v-text-field v-model="brand.name" label="请输入品牌名称" required />
    <v-text-field v-model="brand.letter" label="请输入品牌首字母" required />
  </v-form>
```

- 千万不要忘了通过`v-model`把表单项与`brand`的属性关联起来。

效果：

![1526131172190](assets/1526131172190.png)

#### 1.1.4.3.级联下拉选框

接下来就是商品分类了，按照刚才的分析，商品分类应该是下拉选框。

但是大家仔细思考，商品分类包含三级。在展示的时候，应该是先由用户选中1级，才显示2级；选择了2级，才显示3级。形成一个多级分类的三级联动效果。

这个时候，就不是普通的下拉选框，而是三级联动的下拉选框！

这样的选框，在Vuetify中并没有提供（它提供的是基本的下拉框）。因此我已经给大家编写了一个无限级联动的下拉选框，能够满足我们的需求。

 ![1526131637045](assets/1526131637045.png)

具体请参考课前资料的《自定义组件用法指南.md》

我们在代码中使用：

```js
    <v-cascader
      url="/item/category/list"
      multiple 
      required
      v-model="brand.categories"
      label="请选择商品分类"/>
```

- url：加载商品分类选项的接口路径
- multiple：是否多选，这里设置为true，因为一个品牌可能有多个分类
- requried：是否是必须的，这里为true，会在提示上加*，提醒用户
- v-model：关联我们brand对象的categories属性
- label：文字说明



效果：

 ![1526132934902](assets/1526132934902.png)

data中获取的结果：

 ![1526133224362](assets/1526133224362.png)



#### 1.1.4.4.文件上传项

在Vuetify中，也没有文件上传的组件。

 ![img](assets/0B26B319.gif) 

还好，我已经给大家写好了一个文件上传的组件：

 ![1526133576597](assets/1526133576597.png)

详细用法，参考《自定义组件使用指南.md》

我们添加上传的组件：

```html
<v-layout row>
    <v-flex xs3>
        <span style="font-size: 16px; color: #444">品牌LOGO：</span>
    </v-flex>
    <v-flex>
        <v-upload
             v-model="brand.image"
             url="/upload" 
             :multiple="false" 
             :pic-width="250" 
             :pic-height="90"
                  />
    </v-flex>
</v-layout>
```

注意：

- 文件上传组件本身没有提供文字提示。因此我们需要自己添加一段文字说明
- 我们要实现文字和图片组件左右放置，因此这里使用了`v-layout`布局组件：
  - layout添加了row属性，代表这是一行，如果是column，代表是多行
  - layout下面有`v-flex`组件，是这一行的单元，我们有2个单元
    - `<v-flex xs3>` ：显示文字说明，xs3是响应式布局，代表占12格中的3格
    - 剩下的部分就是图片上传组件了
- `v-upload`：图片上传组件，包含以下属性：
  - v-model：将上传的结果绑定到brand的image属性
  - url：上传的路径，我们先随便写一个。
  - multiple：是否运行多图片上传，这里是false。因为品牌LOGO只有一个
  - pic-width和pic-height：可以控制l图片上传后展示的宽高

最终结果：

 ![1526136024649](assets/1526136024649.png)



#### 1.1.4.5.按钮

上面已经把所有的表单项写完。最后就差提交和清空的按钮了。

在表单的最下面添加两个按钮：

```html
    <v-layout class="my-4" row>
      <v-spacer/>
      <v-btn @click="submit" color="primary">提交</v-btn>
      <v-btn @click="clear" >重置</v-btn>
    </v-layout>
```

- 通过layout来进行布局，`my-4`增大上下边距
- `v-spacer`占用一定空间，将按钮都排挤到页面右侧
- 两个按钮分别绑定了submit和clear事件

我们先将方法定义出来：

```js
methods:{
    submit(){
        // 提交表单
    },
    clear(){
        // 重置表单
    }
}
```

重置表单相对简单，因为v-form组件已经提供了reset方法，用来清空表单数据。只要我们拿到表单组件对象，就可以调用方法了。

我们可以通过`$refs`内置对象来获取表单组件。

首先，在表单上定义`ref`属性：

 ![1526137891067](assets/1526137891067.png)

然后，在页面查看`this.$refs`属性：

![1526138030853](assets/1526138030853.png)

看到`this.$refs`中只有一个属性，就是`myBrandForm`

我们在clear中来获取表单对象并调用reset方法：

```js
    methods:{
      submit(){
        // 提交表单
        console.log(this);
      },
      clear(){
        // 重置表单
        this.$refs.myBrandForm.reset();
        // 需要手动清空商品分类
        this.categories = [];
      }
    }
```

要注意的是，这里我们还手动把this.categories清空了，因为我写的级联选择组件并没有跟表单结合起来。需要手动清空。

### 1.1.5.表单校验

#### 1.1.5.1.校验规则

Vuetify的表单校验，是通过rules属性来指定的：

![1526138441735](assets/1526138441735.png)

校验规则的写法：

![1526138475159](assets/1526138475159.png)

说明：

- 规则是一个数组
- 数组中的元素是一个函数，该函数接收表单项的值作为参数，函数返回值两种情况：
  - 返回true，代表成功，
  - 返回错误提示信息，代表失败

#### 1.1.5.2.项目中代码

我们有四个字段：

- name：做非空校验和长度校验，长度必须大于1
- letter：首字母，校验长度为1，非空。
- image：图片，不做校验，图片可以为空
- categories：非空校验，自定义组件已经帮我们完成，不用写了

首先，我们定义规则：

```js
nameRules:[
    v => !!v || "品牌名称不能为空",
    v => v.length > 1 || "品牌名称至少2位"
],
letterRules:[
    v => !!v || "首字母不能为空",
    v => /^[A-Z]{1}$/.test(v) || "品牌字母只能是A~Z的大写字母"
]
```

然后，在页面标签中指定：

```html
<v-text-field v-model="brand.name" label="请输入品牌名称" required :rules="nameRules" />
<v-text-field v-model="brand.letter" label="请输入品牌首字母" required :rules="letterRules" />
```

效果：

 ![1526139379209](assets/1526139379209.png)



### 1.1.6.表单提交

在submit方法中添加表单提交的逻辑：

```js
submit() {
    // 1、表单校验
    if (this.$refs.myBrandForm.validate()) {
        // 2、定义一个请求参数对象，通过解构表达式来获取brand中的属性
        const {categories ,letter ,...params} = this.brand;
        // 3、数据库中只要保存分类的id即可，因此我们对categories的值进行处理,只保留id，并转为字符串
        params.cids = categories.map(c => c.id).join(",");
        // 4、将字母都处理为大写
        params.letter = letter.toUpperCase();
        // 5、将数据提交到后台
        this.$http.post('/item/brand', params)
            .then(() => {
            // 6、弹出提示
            this.$message.success("保存成功！");
        })
            .catch(() => {
            this.$message.error("保存失败！");
        });
    }
}
```

- 1、通过`this.$refs.myBrandForm`选中表单，然后调用表单的`validate`方法，进行表单校验。返回boolean值，true代表校验通过

- 2、通过解构表达式来获取brand中的值，categories和letter需要处理，单独获取。其它的存入params对象中

- 3、品牌和商品分类的中间表只保存两者的id，而brand.categories中保存的数对象数组，里面有id和name属性，因此这里通过数组的map功能转为id数组，然后通过join方法拼接为字符串

- 4、首字母都处理为大写保存

- 5、发起请求

- 6、弹窗提示成功还是失败，这里用到的是我们的自定义组件功能message组件：

   ![1526140298249](assets/1526140298249.png)

  这个插件把`$message`对象绑定到了Vue的原型上，因此我们可以通过`this.$message`来直接调用。

  包含以下常用方法：

  - info、error、success、warning等，弹出一个带有提示信息的窗口，色调与为普通（灰）、错误（红色）、成功（绿色）和警告（黄色）。使用方法：this.$message.info("msg")
  - confirm：确认框。用法：`this.$message.confirm("确认框的提示信息")`，返回一个Promise

## 1.2.后台实现新增

### 1.2.1.controller

还是一样，先分析四个内容：

- 请求方式：刚才看到了是POST
- 请求路径：/brand
- 请求参数：brand对象，外加商品分类的id数组cids
- 返回值：无

代码：

```java
/**
 * 新增品牌
 * @param brand
 * @return
 */
@PostMapping
public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids") List<Long> cids) {
    this.brandService.saveBrand(brand, cids);
    return new ResponseEntity<>(HttpStatus.CREATED);
}
```



### 1.2.2.Service

这里要注意，我们不仅要新增品牌，还要维护品牌和商品分类的中间表。

```java
@Transactional
public void saveBrand(Brand brand, List<Long> cids) {
    // 新增品牌信息
    this.brandMapper.insertSelective(brand);
    // 新增品牌和分类中间表
    for (Long cid : cids) {
        this.brandMapper.insertCategoryBrand(cid, brand.getId());
    }
}
```

这里调用了brandMapper中的一个自定义方法，来实现中间表的数据新增

### 1.2.3.Mapper

通用Mapper只能处理单表，也就是Brand的数据，因此我们手动编写一个方法及sql，实现中间表的新增：

```java
public interface BrandMapper extends Mapper<Brand> {
    /**
     * 新增商品分类和品牌中间表数据
     * @param cid 商品分类id
     * @param bid 品牌id
     * @return
     */
    @Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES (#{cid},#{bid})")
    int insertCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);
}
```

## 1.3.请求参数格式错误

### 1.3.1.原因分析

我们填写表单并提交，发现报错了：

 ![1526180888663](assets/1526180888663.png)

查看控制台的请求详情：

 ![1526180937974](assets/1526180937974.png)



发现请求的数据格式是JSON格式。

> 原因分析：

axios处理请求体的原则会根据请求数据的格式来定：

- 如果请求体是对象：会转为json发送

- 如果请求体是String：会作为普通表单请求发送，但需要我们自己保证String的格式是键值对。

  如：name=jack&age=12

### 1.3.2.QS工具

QS是一个第三方库，我们可以用`npm install qs --save`来安装。不过我们在项目中已经集成了，大家无需安装：

 ![1526181889564](assets/1526181889564.png)

这个工具的名字：QS，即Query String，请求参数字符串。

什么是请求参数字符串？例如： name=jack&age=21

QS工具可以便捷的实现 JS的Object与QueryString的转换。



在我们的项目中，将QS注入到了Vue的原型对象中，我们可以通过`this.$qs`来获取这个工具：

我们将`this.$qs`对象打印到控制台：

```js
created(){
    console.log(this.$qs);
}
```

发现其中有3个方法：

 ![1526181747560](assets/1526181747560.png)

这里我们要使用的方法是stringify，它可以把Object转为QueryString。



测试一下，使用浏览器工具，把qs对象保存为一个临时变量：

 ![1526182053758](assets/1526182053758.png)

然后调用stringify方法：

 ![1526182230872](assets/1526182230872.png)

成功将person对象变成了 name=jack&age=21的字符串了



### 1.3.3.解决问题

修改页面，对参数处理后发送：

![1526181301670](assets/1526181301670.png)

然后再次发起请求：

 ![1526181331443](assets/1526181331443.png)

发现请求成功：

 ![1526181358204](assets/1526181358204.png)

参数格式：

 ![1526181384653](assets/1526181384653.png)

数据库：

 ![1526181553737](assets/1526181553737.png)



## 1.4.新增完成后关闭窗口

我们发现有一个问题：新增不管成功还是失败，窗口都一致在这里，不会关闭。

这样很不友好，我们希望如果新增失败，窗口保持；但是新增成功，窗口关闭才对。



因此，我们需要**在新增的ajax请求完成以后，关闭窗口**

但问题在于，控制窗口是否显示的标记在父组件：MyBrand.vue中。子组件如何才能操作父组件的属性？或者告诉父组件该关闭窗口了？



之前我们讲过一个父子组件的通信，有印象吗？

- 第一步，在父组件中定义一个函数，用来关闭窗口，不过之前已经定义过了，我们优化一下，关闭的同时重新加载数据：

```js
closeWindow(){
    // 关闭窗口
    this.show = false;
    // 重新加载数据
    this.getDataFromServer();
}
```

- 第二步，父组件在使用子组件时，绑定事件，关联到这个函数：

```html
<!--对话框的内容，表单-->
<v-card-text class="px-5">
    <my-brand-form @close="closeWindow"/>
</v-card-text>
```

- 第三步，子组件通过`this.$emit`调用父组件的函数：

 ![1526216993249](assets/1526216993249.png)



测试一下





# 2.实现图片上传

刚才的新增实现中，我们并没有上传图片，接下来我们一起完成图片上传逻辑。

文件的上传并不只是在品牌管理中有需求，以后的其它服务也可能需要，因此我们创建一个独立的微服务，专门处理各种上传。

## 2.1.搭建项目

### 2.1.1.创建module

![1526192299113](assets/1526192299113.png)

![1526192347113](assets/1526192347113.png)

### 2.1.2.依赖

我们需要EurekaClient和web依赖：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>leyou</artifactId>
        <groupId>com.leyou.parent</groupId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.leyou.service</groupId>
    <artifactId>ly-upload</artifactId>
    <version>1.0.0-SNAPSHOT</version>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>
</project>
```

### 2.1.3.编写配置

```yaml
server:
  port: 8082
spring:
  application:
    name: upload-service
  servlet:
    multipart:
      max-file-size: 5MB # 限制文件上传的大小
# Eureka
eureka:
  client:
    service-url:
      defaultZone: http://127.0.0.1:10086/eureka
  instance:
    lease-renewal-interval-in-seconds: 5 # 每隔5秒发送一次心跳
    lease-expiration-duration-in-seconds: 10 # 10秒不发送就过期
    prefer-ip-address: true
    ip-address: 127.0.0.1
    instance-id: ${spring.application.name}:${server.port}
```

需要注意的是，我们应该添加了限制文件大小的配置

### 2.1.4.启动类

```java
@SpringBootApplication
@EnableDiscoveryClient
public class LyUploadService {
    public static void main(String[] args) {
        SpringApplication.run(LyUploadService.class, args);
    }
}

```

结构：

 ![1526192931088](assets/1526192931088.png)



## 2.2.编写上传功能

### 2.2.1.controller

编写controller需要知道4个内容：

- 请求方式：上传肯定是POST
- 请求路径：/upload/image
- 请求参数：文件，参数名是file，SpringMVC会封装为一个接口：MultipleFile
- 返回结果：上传成功后得到的文件的url路径

代码如下：

```java
@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    /**
     * 上传图片功能
     * @param file
     * @return
     */
    @PostMapping("image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String url = this.uploadService.upload(file);
        if (StringUtils.isBlank(url)) {
            // url为空，证明上传失败
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // 返回200，并且携带url路径
        return ResponseEntity.ok(url);
    }
}
```



### 2.2.2.service

在上传文件过程中，我们需要对上传的内容进行校验：

1. 校验文件大小
2. 校验文件的媒体类型
3. 校验文件的内容

文件大小在Spring的配置文件中设置，因此已经会被校验，我们不用管。

具体代码：

```java
@Service
public class UploadService {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    // 支持的文件类型
    private static final List<String> suffixes = Arrays.asList("image/png", "image/jpeg");

    public String upload(MultipartFile file) {
        try {
            // 1、图片信息校验
            // 1)校验文件类型
            String type = file.getContentType();
            if (!suffixes.contains(type)) {
                logger.info("上传失败，文件类型不匹配：{}", type);
                return null;
            }
            // 2)校验图片内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                logger.info("上传失败，文件内容不符合要求");
                return null;
            }
            // 2、保存图片
            // 2.1、生成保存目录
            File dir = new File("D:\\heima\\upload");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // 2.2、保存图片
            file.transferTo(new File(dir, file.getOriginalFilename()));

            // 2.3、拼接图片地址
            String url = "http://image.leyou.com/upload/" + file.getOriginalFilename();

            return url;
        } catch (Exception e) {
            return null;
        }
    }
}
```



这里有一个问题：为什么图片地址需要使用另外的url？

- 图片不能保存在服务器内部，这样会对服务器产生额外的加载负担
- 一般静态资源都应该使用独立域名，这样访问静态资源时不会携带一些不必要的cookie，减小请求的数据量



### 2.2.3.测试上传

我们通过RestClient工具来测试：

 ![1526196967376](assets/1526196967376.png)

结果：

 ![1526197027688](assets/1526197027688.png)

去目录下查看：

 ![1526197060729](assets/1526197060729.png)

上传成功！



### 2.2.4.绕过网关

图片上传是文件的传输，如果也经过Zuul网关的代理，文件就会经过多次网路传输，造成不必要的网络负担。在高并发时，可能导致网络阻塞，Zuul网关不可用。这样我们的整个系统就瘫痪了。

所以，我们上传文件的请求就不经过网关来处理了。

#### 2.2.4.1.Zuul的路由过滤

Zuul中提供了一个ignored-patterns属性，用来忽略不希望路由的URL路径，示例：

```properties
zuul.ignored-patterns: /upload/**
```

路径过滤会对一切微服务进行判定。

Zuul还提供了`ignored-services`属性，进行服务过滤：

```properties
zuul.ignored-services: upload-servie
```

我们这里采用忽略服务：

```yaml
zuul:
  ignored-services:
    - upload-service # 忽略upload-service服务
```

上面的配置采用了集合语法，代表可以配置多个

#### 2.2.4.2.Nginx的rewrite指令

现在，我们修改页面的访问路径：

```html
<v-upload
      v-model="brand.image" 
      url="/upload/image" 
      :multiple="false" 
      :pic-width="250" :pic-height="90"
      />
```

查看页面的请求路径：

 ![1526196446765](assets/1526196446765.png)

可以看到这个地址不对，依然是去找Zuul网关，因为我们的系统全局配置了URL地址。怎么办？

有同学会想：修改页面请求地址不就好了。

注意：原则上，我们是不能把除了网关以外的服务对外暴露的，不安全。



既然不能修改页面请求，那么就只能在Nginx反向代理上做文章了。

我们修改nginx配置，将以/api/upload开头的请求拦截下来，转交到真实的服务地址:

```nginx
location /api/upload {
    proxy_pass http://127.0.0.1:8082;
    proxy_connect_timeout 600;
    proxy_read_timeout 600;
}
```

这样写大家觉得对不对呢？



显然是不对的，因为ip和端口虽然对了，但是路径没变，依然是：http://127.0.0.1:8002/api/upload/image

前面多了一个/api



Nginx提供了rewrite指令，用于对地址进行重写，语法规则：

```
rewrite "用来匹配路径的正则" 重写后的路径 [指令];
```

我们的案例：

```nginx 
	server {
        listen       80;
        server_name  api.leyou.com;

        proxy_set_header X-Forwarded-Host $host;
        proxy_set_header X-Forwarded-Server $host;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;

    	# 上传路径的映射
		location /api/upload {	
			proxy_pass http://127.0.0.1:8082;
			proxy_connect_timeout 600;
			proxy_read_timeout 600;
			
			rewrite "^/api/(.*)$" /$1 break; 
        }
		
        location / {
			proxy_pass http://127.0.0.1:10010;
			proxy_connect_timeout 600;
			proxy_read_timeout 600;
        }
    }
```

- 首先，我们映射路径是/api/upload，而下面一个映射路径是 / ，根据最长路径匹配原则，/api/upload优先级更高。也就是说，凡是以/api/upload开头的路径，都会被第一个配置处理

- `proxy_pass`：反向代理，这次我们代理到8082端口，也就是upload-service服务

- `rewrite "^/api/(.*)$" /$1 break`，路径重写：

  - `"^/api/(.*)$"`：匹配路径的正则表达式，用了分组语法，把`/api/`以后的所有部分当做1组

  - `/$1`：重写的目标路径，这里用$1引用前面正则表达式匹配到的分组（组编号从1开始），即`/api/`后面的所有。这样新的路径就是除去`/api/`以外的所有，就达到了去除`/api`前缀的目的

  - `break`：指令，常用的有2个，分别是：last、break

    - last：重写路径结束后，将得到的路径重新进行一次路径匹配
    - break：重写路径结束后，不再重新匹配路径。

    我们这里不能选择last，否则以新的路径/upload/image来匹配，就不会被正确的匹配到8082端口了

修改完成，输入`nginx -s reload`命令重新加载配置。然后再次上传试试。

### 2.2.5.跨域问题

重启nginx，再次上传，发现报错了：

![1526200471676](assets/1526200471676.png)

不过庆幸的是，这个错误已经不是第一次见了，跨域问题。

我们在upload-service中添加一个CorsFilter即可：

```java
@Configuration
public class GlobalCorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        //1.添加CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        //1) 允许的域,不要写*，否则cookie就无法使用了
        config.addAllowedOrigin("http://manage.leyou.com");
        //2) 是否发送Cookie信息
        config.setAllowCredentials(false);
        //3) 允许的请求方式
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("POST");
        config.addAllowedHeader("*");

        //2.添加映射路径，我们拦截一切请求
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);

        //3.返回新的CorsFilter.
        return new CorsFilter(configSource);
    }
}
```



再次测试：

 ![1526200606487](assets/1526200606487.png)

不过，非常遗憾的是，访问图片地址，却没有响应。

 ![1526200927268](assets/1526200927268.png)

这是因为我们并没有任何服务器对应image.leyou.com这个域名。。

这个问题，我们暂时放下，回头再来解决。

### 2.2.6.之前上传的缺陷

先思考一下，之前上传的功能，有没有什么问题？

上传本身没有任何问题，问题出在保存文件的方式，我们是保存在服务器机器，就会有下面的问题：

- 单机器存储，存储能力有限
- 无法进行水平扩展，因为多台机器的文件无法共享,会出现访问不到的情况
- 数据没有备份，有单点故障风险
- 并发能力差

这个时候，最好使用分布式文件存储来代替本地文件存储。

# 3.FastDFS

## 3.1.什么是分布式文件系统

分布式文件系统（Distributed File System）是指文件系统管理的物理存储资源不一定直接连接在本地节点上，而是通过计算机网络与节点相连。 

通俗来讲：

- 传统文件系统管理的文件就存储在本机。
- 分布式文件系统管理的文件存储在很多机器，这些机器通过网络连接，要被统一管理。无论是上传或者访问文件，都需要通过管理中心来访问

## 3.2.什么是FastDFS

FastDFS是由淘宝的余庆先生所开发的一个轻量级、高性能的开源分布式文件系统。用纯C语言开发，功能丰富：

- 文件存储
- 文件同步
- 文件访问（上传、下载）
- 存取负载均衡
- 在线扩容

适合有大容量存储需求的应用或系统。同类的分布式文件系统有谷歌的GFS、HDFS（Hadoop）、TFS（淘宝）等。

## 3.3.FastDFS的架构

### 3.3.1.架构图

先上图：

 ![1526205318630](assets/1526205318630.png)

FastDFS两个主要的角色：Tracker Server 和 Storage Server 。

- Tracker Server：跟踪服务器，主要负责调度storage节点与client通信，在访问上起负载均衡的作用，和记录storage节点的运行状态，是连接client和storage节点的枢纽。 
- Storage Server：存储服务器，保存文件和文件的meta data（元数据），每个storage server会启动一个单独的线程主动向Tracker cluster中每个tracker server报告其状态信息，包括磁盘使用情况，文件同步情况及文件上传下载次数统计等信息
- Group：文件组，多台Storage Server的集群。上传一个文件到同组内的一台机器上后，FastDFS会将该文件即时同步到同组内的其它所有机器上，起到备份的作用。不同组的服务器，保存的数据不同，而且相互独立，不进行通信。 
- Tracker Cluster：跟踪服务器的集群，有一组Tracker Server（跟踪服务器）组成。
- Storage Cluster ：存储集群，有多个Group组成。

### 3.3.2.上传和下载流程

> 上传

 ![1526205664373](assets/1526205664373.png)

1. Client通过Tracker server查找可用的Storage server。
2. Tracker server向Client返回一台可用的Storage server的IP地址和端口号。
3. Client直接通过Tracker server返回的IP地址和端口与其中一台Storage server建立连接并进行文件上传。
4. 上传完成，Storage server返回Client一个文件ID，文件上传结束。

> 下载

 ![1526205705687](assets/1526205705687.png)

1. Client通过Tracker server查找要下载文件所在的的Storage server。
2. Tracker server向Client返回包含指定文件的某个Storage server的IP地址和端口号。
3. Client直接通过Tracker server返回的IP地址和端口与其中一台Storage server建立连接并指定要下载文件。
4. 下载文件成功。



## 3.4.安装和使用

参考课前资料的：《centos安装FastDFS.md》

 ![1526205975025](assets/1526205975025.png)



## 3.5.java客户端

余庆先生提供了一个Java客户端，但是作为一个C程序员，写的java代码可想而知。而且已经很久不维护了。

这里推荐一个开源的FastDFS客户端，支持最新的SpringBoot2.0。

配置使用极为简单，支持连接池，支持自动生成缩略图，狂拽酷炫吊炸天啊，有木有。

地址：[tobato/FastDFS_client](https://github.com/tobato/FastDFS_Client)

 ![1526206304954](assets/1526206304954.png)

### 3.5.1.引入依赖

在父工程中，我们已经管理了依赖，版本为：

```xml
<fastDFS.client.version>1.26.2</fastDFS.client.version>
```

因此，这里我们直接引入坐标即可：

```xml
<dependency>
    <groupId>com.github.tobato</groupId>
    <artifactId>fastdfs-client</artifactId>
</dependency>
```



### 3.5.2.引入配置类

纯java配置：

```java
@Configuration
@Import(FdfsClientConfig.class)
// 解决jmx重复注册bean的问题
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class FastClientImporter {
}
```

### 3.5.3.编写FastDFS属性

```yaml
fdfs:
  so-timeout: 1501
  connect-timeout: 601
  thumb-image: # 缩略图
    width: 60
    height: 60
  tracker-list: # tracker地址
    - 192.168.56.101:22122
```

### 3.5.4.测试

```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LyUploadService.class)
public class FdfsTest {

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private ThumbImageConfig thumbImageConfig;

    @Test
    public void testUpload() throws FileNotFoundException {
        File file = new File("D:\\test\\baby.png");
        // 上传并且生成缩略图
        StorePath storePath = this.storageClient.uploadFile(
                new FileInputStream(file), file.length(), "png", null);
        // 带分组的路径
        System.out.println(storePath.getFullPath());
        // 不带分组的路径
        System.out.println(storePath.getPath());
    }

    @Test
    public void testUploadAndCreateThumb() throws FileNotFoundException {
        File file = new File("D:\\test\\baby.png");
        // 上传并且生成缩略图
        StorePath storePath = this.storageClient.uploadImageAndCrtThumbImage(
                new FileInputStream(file), file.length(), "png", null);
        // 带分组的路径
        System.out.println(storePath.getFullPath());
        // 不带分组的路径
        System.out.println(storePath.getPath());
        // 获取缩略图路径
        String path = thumbImageConfig.getThumbImagePath(storePath.getPath());
        System.out.println(path);
    }
}
```

结果：

```
group1/M00/00/00/wKg4ZVro5eCAZEMVABfYcN8vzII630.png
M00/00/00/wKg4ZVro5eCAZEMVABfYcN8vzII630.png
M00/00/00/wKg4ZVro5eCAZEMVABfYcN8vzII630_60x60.png
```

访问第一个路径：

![1526215187172](assets/1526215187172.png)

访问最后一个路径（缩略图路径），注意加组名：

 ![1526215257110](assets/1526215257110.png)



### 3.5.5.改造上传逻辑

```java
@Service
public class UploadService {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    // 支持的文件类型
    private static final List<String> suffixes = Arrays.asList("image/png", "image/jpeg");

    @Autowired
    FastFileStorageClient storageClient;

    public String upload(MultipartFile file) {
        try {
            // 1、图片信息校验
            // 1)校验文件类型
            String type = file.getContentType();
            if (!suffixes.contains(type)) {
                logger.info("上传失败，文件类型不匹配：{}", type);
                return null;
            }
            // 2)校验图片内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                logger.info("上传失败，文件内容不符合要求");
                return null;
            }

            // 2、将图片上传到FastDFS
            // 2.1、获取文件后缀名
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            // 2.2、上传
            StorePath storePath = this.storageClient.uploadFile(
                    file.getInputStream(), file.getSize(), extension, null);
            // 2.3、返回完整路径
            return "http://image.leyou.com/" + storePath.getFullPath();
        } catch (Exception e) {
            return null;
        }
    }
}
```



只需要把原来保存文件的逻辑去掉，然后上传到FastDFS即可。



### 3.5.6.测试

通过RestClient测试：

 ![1526215940805](assets/1526215940805.png)

## 3.6.页面测试上传

发现上传成功：

 ![1526216133300](assets/1526216133300.png)

不过，当我们访问页面时：

 ![1526216178123](assets/1526216178123.png)

这是因为我们图片是上传到虚拟机的，ip为：192.168.56.101

因此，我们需要将image.leyou.com映射到192.168.56.101

修改我们的hosts：

 ![1526216272835](assets/1526216272835.png)



再次上传：

 ![1526216322359](assets/1526216322359.png)



# 4.修改品牌（作业）

修改的难点在于回显。

当我们点击编辑按钮，希望弹出窗口的同时，看到原来的数据：

![1526216494380](assets/1526216494380.png)



## 4.1.点击编辑出现弹窗

这个比较简单，修改show属性为true即可实现，我们绑定一个点击事件：

```html
<v-btn color="info" @click="editBrand">编辑</v-btn>
```

然后编写事件，改变show 的状态：

 ![1526217622765](assets/1526217622765.png)

如果仅仅是这样，编辑按钮与新增按钮将没有任何区别，关键在于，如何回显呢？

## 4.2.回显数据

回显数据，就是把当前点击的品牌数据传递到子组件（MyBrandForm）。而父组件给子组件传递数据，通过props属性。

- 第一步：在编辑时获取当前选中的品牌信息，并且记录到data中

  先在data中定义属性，用来接收用来编辑的brand数据：

   ![1526218080029](assets/1526218080029.png)

  我们在页面触发编辑事件时，把当前的brand传递给editBrand方法：

  ```html
  <v-btn color="info" @click="editBrand(props.item)">编辑</v-btn>
  ```

  然后在editBrand中接收数据，赋值给oldBrand：

  ```js
  editBrand(oldBrand){
    // 控制弹窗可见：
    this.show = true;
    // 获取要编辑的brand
    this.oldBrand = oldBrand;
  },
  ```

- 第二步：把获取的brand数据 传递给子组件

  ```html
  <!--对话框的内容，表单-->
  <v-card-text class="px-5">
      <my-brand-form @close="closeWindow" :oldBrand="oldBrand"/>
  </v-card-text>
  ```

- 第三步：在子组件中通过props接收要编辑的brand数据，Vue会自动完成回显

  接收数据：

   ![1526218243761](assets/1526218243761.png)

  通过watch函数监控oldBrand的变化，把值copy到本地的brand：

  ```js
  watch: {
      oldBrand: {// 监控oldBrand的变化
          handler(val) {
              if(val){
                  // 注意不要直接复制，否则这边的修改会影响到父组件的数据，copy属性即可
                  this.brand =  Object.deepCopy(val)
              }else{
                  // 为空，初始化brand
                  this.brand = {
                      name: '',
                      letter: '',
                      image: '',
                      categories: [],
                  }
              }
          },
              deep: true
      }
  }
  ```

  - Object.deepCopy 自定义的对对象进行深度复制的方法。
  - 需要判断监听到的是否为空，如果为空，应该进行初始化



测试：发现数据回显了，除了商品分类以外：

 ![1526219653994](assets/1526219653994.png)



## 4.3.商品分类回显

为什么商品分类没有回显？

因为品牌中并没有商品分类数据。我们需要在进入编辑页面之前，查询商品分类信息：

### 4.3.1.后台提供接口

> #### controller

```java
/**
     * 通过品牌id查询商品分类
     * @param bid
     * @return
     */
@GetMapping("bid/{bid}")
public ResponseEntity<List<Category>> queryByBrandId(@PathVariable("bid") Long bid) {
    List<Category> list = this.categoryService.queryByBrandId(bid);
    if (list == null || list.size() < 1) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok(list);
}
```



> Service

```java
public List<Category> queryByBrandId(Long bid) {
    return this.categoryMapper.queryByBrandId(bid);
}
```



> mapper

因为需要通过中间表进行子查询，所以这里要手写Sql：

```java
/**
     * 根据品牌id查询商品分类
     * @param bid
     * @return
     */
@Select("SELECT * FROM tb_category WHERE id IN (SELECT category_id FROM tb_category_brand WHERE brand_id = #{bid})")
List<Category> queryByBrandId(Long bid);
```

### 4.3.2.前台查询分类并渲染

我们在编辑页面打开之前，先把数据查询完毕：

```js
editBrand(oldBrand){
    // 根据品牌信息查询商品分类
    this.$http.get("/item/category/bid/" + oldBrand.id)
        .then(({data}) => {
        // 控制弹窗可见：
        this.show = true;
        // 获取要编辑的brand
        this.oldBrand = oldBrand
        // 回显商品分类
        this.oldBrand.categories = data;
    })
}
```



再次测试：数据成功回显了

 ![1526222999115](assets/1526222999115.png)



### 4.3.3.新增窗口数据干扰

但是，此时却产生了新问题：新增窗口竟然也有数据？

原因：

​	如果之前打开过编辑，那么在父组件中记录的oldBrand会保留。下次再打开窗口，如果是编辑窗口到没问题，但是新增的话，就会再次显示上次打开的品牌信息了。



解决：

​	新增窗口打开前，把数据置空。

```js
addBrand() {
    // 控制弹窗可见：
    this.show = true;
    // 把oldBrand变为null
    this.oldBrand = null;
}
```



### 4.3.4.提交表单时判断是新增还是修改

新增和修改是同一个页面，我们该如何判断？

父组件中点击按钮弹出新增或修改的窗口，因此父组件非常清楚接下来是新增还是修改。

因此，最简单的方案就是，在父组件中定义变量，记录新增或修改状态，当弹出页面时，把这个状态也传递给子组件。

第一步：在父组件中记录状态：

 ![1526224372366](assets/1526224372366.png)



第二步：在新增和修改前，更改状态：

 ![1526224447288](assets/1526224447288.png)

第三步：传递给子组件

 ![1526224495244](assets/1526224495244.png)

第四步，子组件接收标记：

 ![1526224563838](assets/1526224563838.png)



标题的动态化：

 ![1526224628514](assets/1526224628514.png)

表单提交动态：

axios除了除了get和post外，还有一个通用的请求方式：

```js
// 将数据提交到后台
// this.$http.post('/item/brand', this.$qs.stringify(params))
this.$http({
    method: this.isEdit ? 'put' : 'post', // 动态判断是POST还是PUT
    url: '/item/brand',
    data: this.$qs.stringify(this.brand)
}).then(() => {
    // 关闭窗口
    this.$emit("close");
    this.$message.success("保存成功！");
})
    .catch(() => {
    this.$message.error("保存失败！");
});
```







# 5.删除（作业）