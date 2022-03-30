package project.tangchen;

import cn.cheny.toolbox.POIUtils.PoiUtils;
import cn.cheny.toolbox.POIUtils.annotation.ExcelCell;
import cn.cheny.toolbox.POIUtils.annotation.ExcelHead;
import cn.cheny.toolbox.POIUtils.entity.ExcelReadInfo;
import cn.cheny.toolbox.POIUtils.entity.ReadResult;
import com.alibaba.fastjson.JSONObject;
import com.yy.shopline.translation.common.bean.BaseResponse;
import com.yy.shopline.translation.common.utils.http.HttpUtils;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.core.ParameterizedTypeReference;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author by chenyi
 * @date 2022/3/25
 */
public class MainTask {

    private static List<String> filters = Arrays.asList(
            "acesulfame k",
            "acetic acid",
            "acetyl dl methionin",
            "n acetyl.",
            "alanine",
            "alcohol",
            "alginate",
            "amonium molybdate",
            "amylase",
            "apple",
            "arginine",
            "ascorbyl palmitate",
            "aspartic acid",
            "bacillus coagulans",
            "barley",
            "beef",
            "beeswax",
            "beetroot",
            "beetroot juice",
            "beta-carotene",
            "bifidobacterium longum",
            "blood",
            "boron",
            "bovine gelatin",
            "brewers yeast",
            "brilliant blue fcf",
            "bromelain",
            "brown rice",
            "brown rice flour",
            "brown rice syrup",
            "buckwheat",
            "cabbage",
            "calcium",
            "calcium ascorbate",
            "calcium carbonate",
            "calcium phosphate",
            "cane sugar",
            "glucose syrup",
            "sugar",
            "caramel color",
            "caramel color class 4 - sulfite ammonia caramel",
            "carbon dioxide",
            "carnauba wax",
            "carrageenan",
            "carrot",
            "cauliflower",
            "cayenne pepper",
            "cellulose gum",
            "chicken",
            "chicory root fiber",
            "chloride",
            "chlorogenic acid",
            "cider",
            "cider vinegar",
            "citric acid",
            "citrulline",
            "coconut oil",
            "cod",
            "cod liver oil",
            "collagen - other",
            "copper",
            "corn",
            "corn starch",
            "corn syrup",
            "croscarmellose sodium",
            "cupric gluconate",
            "cystine (l-)",
            "d-alpha-tocopherol",
            "dextrose",
            "enzymes",
            "enzymes other",
            "ethanol",
            "ethyl cellulose",
            "fennel",
            "ferrous bisglycinate",
            "ferrous fumarate",
            "fiber",
            "fish collagen - other",
            "flavonoid",
            "flavonoids",
            "galacto oligosaccharide",
            "gelatin",
            "gellan gum",
            "genistein",
            "glutamine",
            "glycerol",
            "glyceryl monostearate",
            "glycine",
            "granulated sugar",
            "guar gum",
            "gum arabic",
            "hawthorn",
            "histidine",
            "hydroxyproline",
            "hydroxypropyl cellulose",
            "hydroxypropyl methylcellulose",
            "inulin",
            "iodine",
            "iron",
            "iron oxide red",
            "iron oxide yellow",
            "iron oxides",
            "isoflavones",
            "isoleucine",
            "isomalt",
            "lactase",
            "lactoferrin",
            "lactose",
            "lecithin",
            "lemon",
            "leucine",
            "lipase",
            "liver",
            "l-lysine hydrochloride",
            "lutein",
            "lysine",
            "magnesium",
            "magnesium ascorbate",
            "magnesium aspartate",
            "magnesium bisglycinate",
            "magnesium citrate",
            "magnesium hydroxide",
            "magnesium oxide",
            "magnesium stearate",
            "malic acid",
            "maltodextrin",
            "manganese",
            "manganese sulphate",
            "mango",
            "mannitol",
            "methionine",
            "methyl cellulose",
            "methylsulfonyl methane",
            "msm",
            "microcrystalline cellulose",
            "milk other",
            "millet",
            "modified cellulose gum",
            "mushroom",
            "nettle",
            "oat",
            "oligofructose",
            "olive oil",
            "orange",
            "ornithine",
            "other oils",
            "other starches",
            "other syrups",
            "other, beans",
            "palm oil",
            "palm/coconut sugar",
            "papain",
            "papaya",
            "partially hydrogenated vegetable oil",
            "pea",
            "pea protein - other",
            "pea starch",
            "pectin",
            "peppermint",
            "phenylalanine",
            "phytosterols",
            "pineapple",
            "polyethylene glycols",
            "polyphenols",
            "polyvinyl alcohol",
            "polyvinylpyrrolidone",
            "potassium",
            "potassium citrate",
            "potassium iodide",
            "potassium sorbate",
            "potato starch",
            "powdered cellulose",
            "proline",
            "protease",
            "pullulan",
            "red beetroot extract color",
            "rice",
            "rice concentrate",
            "rice flour",
            "rice powder",
            "rice starch",
            "selenium",
            "serine",
            "silica",
            "silicon dioxide",
            "silicon dioxide.",
            "skimmed milk powder",
            "sodium",
            "sodium alginate",
            "sodium ascorbate",
            "sodium benzoate",
            "sodium carboxymethyl cellulose",
            "enzymatically hydrolysed",
            "cellulose gum",
            "enzymatically hydrolyzed",
            "sodium chloride",
            "sodium citrate",
            "sodium hydroxide",
            "sodium molybdate",
            "sodium selenite",
            "sodium stearyl fumarate",
            "sorbitol",
            "spinach",
            "stevia",
            "succinic acid",
            "sucralose",
            "sunflower oil",
            "sunset yellow fcf",
            "talc",
            "tapioca",
            "tapioca dextrose",
            "tapioca maltodextrin",
            "tapioca syrup",
            "taurine",
            "thaumatin",
            "thiamin nitrate",
            "threonine",
            "titanium dioxide",
            "titanium dioxide color",
            "tocopheryl acetate",
            "tocotrienol",
            "treacle",
            "triacetin",
            "tricalcium phosphate",
            "tryptophan",
            "tuna",
            "valine",
            "vanilla",
            "vegetable",
            "vegetable cellulose",
            "vegetable oil",
            "vinegar",
            "vinegar powder",
            "vitamin a",
            "vitamin b1",
            "vitamin b10",
            "vitamin b12",
            "vitamin b2",
            "vitamin b3",
            "vitamin b5",
            "vitamin b6",
            "vitamin b7",
            "vitamin b8",
            "vitamin b9",
            "vitamin c",
            "vitamin d",
            "vitamin d3",
            "vitamin e",
            "vitamin k",
            "vitamin k2",
            "water",
            "wheatgrass",
            "whey protein - other",
            "whole milk powder",
            "xanthan gum",
            "xylitol",
            "yeast - other",
            "zinc",
            "zinc bisglycinate",
            "zinc citrate",
            "zinc gluconate",
            "zinc oxide",
            ".",
            "purified water",
            "silica.",
            "natural flavor",
            "natural flavors",
            "vitamin c (as ascorbic acid)",
            "root",
            "hypromellose",
            "biotin",
            "fruit",
            "glycerin",
            "vegetable magnesium stearate",
            "stearic acid",
            "vitamin b6 (as pyridoxine hcl)",
            "organic tapioca syrup",
            "magnesium stearate.",
            "zinc (as zinc citrate)",
            "vitamin b12 (as cyanocobalamin)",
            "dicalcium phosphate",
            "organic cane sugar",
            "vegetable glycerin",
            "extract",
            "gelatin capsule",
            "vitamin d (as cholecalciferol)",
            "zinc (as zinc oxide)",
            "lactobacillus acidophilus",
            "vitamin b12 (as methylcobalamin)",
            "lactic acid",
            "vegetable magnesium stearate.",
            "vitamin d3 (as cholecalciferol)",
            "cellulose (vegetable capsule)",
            "other ingredients: microcrystalline cellulose",
            "capsule",
            "sucrose",
            "other ingredients: gelatin",
            "gelatin",
            "folate",
            "niacin (as niacinamide)",
            "vitamin c (ascorbic acid)",
            "cellulose",
            "(root)",
            "(fruit)",
            "sea salt",
            "glucose",
            "vegetable cellulose (capsule)",
            "riboflavin (vitamin b2)",
            "magnesium (as magnesium oxide)",
            "vitamin a (as beta carotene)",
            "niacin",
            "deionized water.",
            "purified water.",
            "thiamine (vitamin b1)",
            "natural colors",
            "vegetable capsule",
            "vegetarian capsule",
            ")",
            "salt",
            "natural and artificial flavors",
            "magnesium stearate (vegetable source)",
            "water.",
            "]",
            "d-mannose",
            "d mannose",
            "anti-caking agents",
            "vegetable stearate",
            "other ingredients: vegetable cellulose (capsule)",
            "other ingredients: capsule",
            "polydextrose",
            "microcrystalline cellulose (filler)",
            "bulking agent (microcrystalline cellulose)",
            "nicotinamide",
            "magnesium salts of fatty acids",
            "anti caking agents",
            "capsule shell",
            "ascorbic acid",
            "flavor",
            "riboflavin",
            "l-ascorbic acid",
            "pyridoxine hydrochloride (vitamin b6)",
            "folic acid",
            "cornstarch",
            "anti caking agent",
            "hydroxypropylmethylcellulose",
            "acidifier: citric acid",
            "bulking agent: microcrystalline cellulose",
            "ascorbic acid (vitamin c)",
            "modified starch",
            "fillers",
            "silicon dioxide (anti caking agent)",
            "capsule shell (hydroxypropyl methylcellulose)",
            "l-ascorbic acid (vitamin c)",
            "sweeteners",
            "anti caking agent (magnesium stearate)",
            "gelling agent (pectin)",
            "thiamine hydrochloride",
            "filler",
            "acidity regulator (citric acid)",
            "copper sulfate",
            "hydroxypropylmethylcellulose (capsule)",
            "minerals",
            "magnesium carbonate",
            "anti-caking agent",
            "bulking agent",
            "thiamine mononitrate",
            "pantothenic acid",
            "acidity regulator: citric acid",
            "humectant (glycerol)",
            "anti caking agent (silicon dioxide)",
            "fructose",
            "l ascorbic acid",
            "osmotic water",
            "bulking agents",
            "sweetener (steviol glycoside)",
            "filler (microcrystalline cellulose)",
            "acid (citric acid)",
            "hydroxypropylmethyl cellulose (capsule)",
            "flavors",
            "vegetable capsule.",
            "d-biotin",
            "choline bitartrate",
            "beta carotene",
            "cyanocobalamin (vitamin b12)",
            "d-biotin.",
            "cyanocobalamin",
            "calcium d-pantothenate",
            "alpha lipoic acid",
            "natural flavoring",
            "sweetener: sucralose",
            "cholecalciferol (vitamin d3)",
            "lactose (milk)",
            "glazing agents",
            "sodium selenite.",
            "glucose (milk)",
            "galactose (milk)",
            "copper citrate",
            "vegetable cellulose (vegetarian)",
            "glazing agent",
            "manganese sulfate",
            "humectant: glycerol",
            "glycerine",
            "sweetener",
            "manganese gluconate",
            "cholecalciferol (vitamin d)",
            "bulking agent: cellulose",
            "flavoring",
            "cholecalciferol.",
            "iron fumarate",
            "hpmc (thickener/capsule shell)",
            "methylcobalamin",
            "acidity regulator (sodium citrate)",
            "anti caking agent: magnesium stearate",
            "steviol glycoside",
            "anti caking agents: magnesium salts of fatty acids",
            "calcium pantothenate",
            "leaves",
            "anti caking agent: silicon dioxide",
            "shell: vegetable capsule (hydroxypropylmethylcellulose)",
            "vegetable capsule: pullulan",
            "methylcobalamin.",
            "methylcobalamin (vitamin b12)",
            "sweetener (sucralose)",
            "maltitol syrup",
            "antioxidant",
            "talc.",
            "gelling agent: pectin",
            "anti caking agent (magnesium salts of fatty acids)",
            "filler: microcrystalline cellulose",
            "natural orange flavor",
            "vegetarian magnesium stearate",
            "erythritol",
            "milk",
            "microcrystalline cellulose (bulking agent)",
            "capsule shell: hydroxypropylmethylcellulose",
            "capsule shell (hydroxypropylmethylcellulose)",
            "anti caking agent: magnesium salts of fatty acids (vegetable)",
            "pteroylmonoglutamic acid (folic acid)",
            "hydroxypropylmethyl cellulose",
            "powder",
            "folic acid (vitamin b9)",
            "folic acid (pteroylmonoglutamic acid)",
            "methylsulfonylmethane",
            "anti-caking agent: silicon dioxide",
            "humectant",
            "acidity regulator",
            "stabilizer: glycerol",
            "magnesium stearate (vegetable anti caking agent)",
            "capsule composition: vegetable cellulose (vegetarian)",
            "silicon dioxide (anti-caking agent)",
            "excipients",
            "vegetarian",
            "magnesium salts of fatty acids.",
            "acidity regulators",
            "anti caking agents: silicon dioxide",
            "10:1",
            "orange flavor",
            "casing: polysaccharide of plant origin (pullulan)",
            "anti-caking agent (silicon dioxide)",
            "vegetable capsule (hydroxypropylmethylcellulose)",
            "hpmc (thickener or capsule shell)",
            "antioxidants",
            "pyridoxine hcl",
            "anti-caking agent (magnesium stearate)",
            "nicotinamide (niacin)",
            "beef gelatin",
            "acidifier (citric acid)",
            "preservative (potassium sorbate)",
            "flavorings",
            "copper bisglycinate",
            "coating agent (gelatin)",
            "coating agent",
            "sweetener: steviol glycoside.",
            "coating",
            "fatty acids",
            "preservatives: sodium benzoate",
            "preservatives",
            "calcium phosphates",
            "vegetable origin capsule (hpmc)",
            "filler (cellulose)",
            "galactooligosaccharides (milk)",
            "coloring",
            "other ingredients: glucose syrup",
            "inactive ingredients: cellulose (vegetable capsule)",
            "calcium (as calcium carbonate)",
            "carnauba wax.",
            "medium chain triglycerides",
            "citrus pectin",
            "vegetable oil (contains carnauba wax)",
            "other ingredients: organic tapioca syrup",
            "magnesium stearate (vegetable)",
            "and silicon dioxide.",
            "natural apple flavor",
            "purple carrot juice concentrate",
            "vegetable stearic acid",
            "chromium (as chromium picolinate)",
            "magnesium (as magnesium citrate)",
            "microcrystalline cellulose.",
            "other ingredients: gelatin (bovine)",
            "other ingredients: hypromellose (capsule)",
            "raw cane sugar",
            "other ingredients: gelatin capsule",
            "gelatin (bovine)",
            "potassium chloride",
            "vegetable magnesium stearate and silicon dioxide.",
            "gum acacia",
            "copper (as copper gluconate)",
            "other ingredients: sugar",
            "pantothenic acid (as d calcium pantothenate)",
            "citric acid.",
            "other ingredients: hypromellose",
            "other ingredients: vegetable cellulose",
            "titanium dioxide (color)",
            "other ingredients: cellulose (vegetable capsule)",
            "other ingredients: hydroxypropyl methylcellulose",
            "pantothenic acid (as calcium d pantothenate)",
            "black pepper extract (piper nigrum)",
            "sodium citrate.",
            "fruit and vegetable juice (color)",
            "polyethylene glycol",
            "thiamine (as thiamine mononitrate)",
            "trisodium citrate",
            "organic natural flavors",
            "acacia gum",
            "other ingredients: vegetable capsule",
            "gelatin (capsule)",
            "vegetable juice (color)",
            "other ingredients: natural flavors",
            "natural raspberry flavor",
            "other ingredients: vegetable cellulose capsule",
            "seed",
            "l theanine",
            "proprietary blend",
            "bark",
            "other ingredients: rice powder",
            "l-theanine",
            "zinc (as zinc gluconate)",
            "aerial",
            "proprietary formula",
            "cellulase",
            "chondroitin sulfate",
            "tyrosine",
            "l-tyrosine",
            "(seed)",
            "pantothenic acid (as calcium pantothenate)",
            "whole plant",
            "flower",
            "herb",
            "iron (as ferrous fumarate)",
            "coconut oil and carnauba wax.",
            "propylene glycol",
            "calcium (dicalcium phosphate)",
            "aerial parts",
            "pantothenic acid (as d-calcium pantothenate)",
            "glucosamine sulfate",
            "extract.",
            "biotin (as d biotin)",
            "l tyrosine",
            "organic inulin",
            "bulb",
            "manganese (as manganese amino acid chelate)",
            "typical amino acid profile: alanine",
            "natural flavor.",
            "hydroxylysine",
            "glucose syrup (tapioca syrup)",
            "copper (as cupric oxide)",
            "manganese (as manganese sulfate)",
            "l arginine",
            "selenium (as selenium amino acid chelate)",
            "titanium dioxide.",
            "(leaf)",
            "probiotic blend",
            "calcium silicate",
            "chromium picolinate",
            "adipic acid",
            "maltitol",
            "thiamine (as thiamine hcl)",
            "natural fruit flavors",
            "other ingredients: tapioca syrup",
            "tocofersolan",
            "l-tryptophan",
            "valine.",
            "povidone",
            "magnesium stearate and silica.",
            "pantothenic acid (as calcium d-pantothenate)",
            "natural lemon flavor.",
            "selenium (as sodium selenite)",
            "modified food starch",
            "as thiamine hcl",
            "l glutamine",
            "cellulose (plant origin)",
            "organic clarified lemon juice concentrate",
            "other ingredients: natural flavor",
            "niacinamide",
            "natural strawberry flavor",
            "folate (as folic acid)",
            "fd and c blue 1.",
            "organic luo han guo (monk fruit)",
            "natural blueberry flavor",
            "calcium stearate",
            "monoammoniated glycyrrhizin",
            "paprika oleoresin (color)",
            "flax seed powder",
            "docosahexaenoic acid (dha)",
            "maca root powder",
            "sodium (as sodium bicarbonate)",
            "calcium (as dicalcium phosphate)",
            "fd and c red 40",
            "cysteine",
            "other ingredients: citric acid",
            "iodine (from kelp)",
            "natural color",
            "rice maltodextrin",
            "strawberry powder",
            "vegetable glycerin.",
            "potassium (as potassium citrate)",
            "medium chain triglyceride (mct)",
            "immune synergy blend",
            "glucose syrup (corn syrup)",
            "cholecalciferol",
            "selenium (as selenomethionine)",
            "potassium sorbate (preservative)",
            "contains less than 2% of: silica",
            "crospovidone",
            "l-leucine",
            "polysorbate 80",
            "l-glutamine",
            "stearic acid (vegetable source)",
            "modified cellulose",
            "resin",
            "cornstarch.",
            "selenium (as l selenomethionine)",
            "sodium benzoate (preservative)",
            "gum base",
            "leaf",
            "l theanine",
            "proprietary blend",
            "iodine (as potassium iodide)",
            "bark",
            "l-theanine",
            "zinc (as zinc gluconate)",
            "aerial",
            "bioperine",
            "glutamic acid",
            "tyrosine",
            "choline (as choline bitartrate)",
            "root extract",
            "l-tyrosine",
            "whole plant",
            "iron (as ferrous fumarate)",
            "coconut oil and carnauba wax.",
            "propylene glycol",
            "calcium (dicalcium phosphate)",
            "aerial parts",
            "pantothenic acid (as d-calcium pantothenate)",
            "glucosamine sulfate",
            "extract.",
            "biotin (as d biotin)",
            "rhizome",
            "bulb",
            "manganese (as manganese amino acid chelate)",
            "typical amino acid profile: alanine",
            "natural flavor.",
            "hydroxylysine",
            "beta carotene.",
            "glucose syrup (tapioca syrup)",
            "copper (as cupric oxide)",
            "manganese (as manganese sulfate)",
            "titanium dioxide.",
            "(leaf)",
            "probiotic blend",
            "other ingredients: rice flour",
            "yellow beeswax",
            "calcium silicate",
            "chromium picolinate",
            "adipic acid",
            "maltitol",
            "folate (folic acid)",
            "natural fruit flavors",
            "other ingredients: natural flavor",
            "other ingredients: tapioca syrup",
            "niacinamide",
            "natural blueberry flavor",
            "sunflower oil.",
            "povidone",
            "magnesium stearate and silica.",
            "natural lemon flavor.",
            "modified food starch",
            "as thiamine hcl",
            "cellulose (plant origin)",
            "natural strawberry flavor",
            "folate (as folic acid)",
            "fd and c blue 1.",
            "calcium stearate",
            "monoammoniated glycyrrhizin",
            "paprika oleoresin (color)",
            "niacin (vitamin b3)",
            "potassium (as potassium gluconate)",
            "glucose syrup (corn syrup)",
            "(herb)",
            "starch",
            "sodium (as sodium bicarbonate)",
            "calcium (as dicalcium phosphate)",
            "fd and c red 40",
            "cysteine",
            "other ingredients: citric acid",
            "iodine (from kelp)",
            "natural color",
            "rice maltodextrin",
            "strawberry powder",
            "potassium (as potassium citrate)",
            "selenium (as selenomethionine)",
            "potassium sorbate (preservative)",
            "contains less than 2% of: silica",
            "crospovidone",
            "l-leucine",
            "polysorbate 80",
            "l-glutamine",
            "stearic acid (vegetable source)",
            "modified cellulose",
            "resin",
            "cornstarch.",
            "selenium (as l selenomethionine)",
            "sodium benzoate (preservative)",
            "daily prebiotic blend",
            "oil",
            "less than 2% of: citric acid",
            "antioxidant blend: spectra",
            "bifido probiotic blend",
            "magnesium (as magnesium glycinate)",
            "other ingredients: maltodextrin",
            "other ingredients: maltitol",
            "light corn syrup",
            "leaf",
            "proprietary blend",
            "l theanine",
            "seed",
            "iodine (as potassium iodide)",
            "l-theanine",
            "zinc (as zinc gluconate)",
            "bark",
            "inositol",
            "aerial",
            "other ingredients: rice powder",
            "bioperine",
            "proprietary formula",
            "choline (as choline bitartrate)",
            "pantothenic acid (as calcium pantothenate)",
            "whole plant",
            "flower",
            "(seed)",
            "iron (as ferrous fumarate)",
            "aerial parts",
            "rhizome",
            "bulb",
            "propylene glycol",
            "l tyrosine",
            "extract.",
            "calcium (dicalcium phosphate)",
            "pantothenic acid (as d-calcium pantothenate)",
            "calcium silicate",
            "other ingredients: rice flour",
            "hydroxylysine",
            "manganese (as manganese amino acid chelate)",
            "folate (folic acid)",
            "natural flavor.",
            "glucose syrup (tapioca syrup)",
            "copper (as cupric oxide)",
            "manganese (as manganese sulfate)",
            "modified food starch",
            "maltitol",
            "titanium dioxide.",
            "l glutamine",
            "(leaf)",
            "probiotic blend",
            "other ingredients: tapioca syrup",
            "natural strawberry flavor",
            "niacin (vitamin b3)",
            "adipic acid",
            "natural lemon flavor.",
            "selenium (as sodium selenite)",
            "other ingredients: citric acid",
            "natural fruit flavors",
            "l taurine",
            "other ingredients: natural flavor",
            "sodium acid sulfate",
            "tocofersolan",
            "magnesium stearate and silica.",
            "as thiamine hcl",
            "ginger root powder",
            "cellulose (plant origin)",
            "other ingredients: maltodextrin",
            "vegetable glycerin.",
            "other ingredients: cellulose",
            "calcium stearate",
            "leaf",
            "l theanine",
            "proprietary blend",
            "rice flour.",
            "iodine (as potassium iodide)",
            "seed",
            "l-theanine",
            "bark",
            "zinc (as zinc gluconate)",
            "aerial",
            "other ingredients: rice powder",
            "(seed)",
            "glutamic acid",
            "root extract",
            "rhizome",
            "(leaf)",
            "proprietary formula",
            "other ingredients: rice flour",
            "flower",
            "whole plant",
            "l arginine",
            "aerial parts",
            "natural flavor.",
            "bulb",
            "amazing grass green food blend",
            "l-arginine",
            "calcium silicate",
            "maltitol",
            "extract.",
            "other ingredients: tapioca syrup",
            "typical amino acid profile: alanine",
            "sodium bicarbonate",
            "yellow beeswax",
            "natural blueberry flavor",
            "titanium dioxide.",
            "berry",
            "oil",
            "natural strawberry flavor",
            "other ingredients: maltodextrin",
            "adipic acid",
            "natural lemon flavor.",
            "calcium (dicalcium phosphate)",
            "biotin (as d biotin)",
            "rice maltodextrin",
            "flavors.",
            "fd and c red 40",
            "probiotic blend",
            "manganese (as manganese citrate)",
            "other ingredients: citric acid",
            "other ingredients: natural flavor",
            "extract (root)",
            "manganese (as manganese sulfate)",
            "potassium sorbate (preservative)",
            "proprietary probiotic blend",
            "papaya fruit powder",
            "copper (as cupric oxide)",
            "glucose syrup (corn syrup)",
            "modified food starch",
            "natural flavors.",
            "niacin (vitamin b3)",
            "pantothenic acid (as calcium d-pantothenate)",
            "glucose syrup (tapioca syrup)",
            "sucralose.",
            "ginger flavor.",
            "fd and c blue 1.",
            "paprika oleoresin (color)",
            "less than 2% of: citric acid",
            "other ingredients: cellulose",
            "leaf",
            "proprietary blend",
            "rice flour.",
            "seed",
            "aerial",
            "bark",
            "zinc (as zinc gluconate)",
            "cellulase",
            "other ingredients: rice powder",
            "root extract",
            "tyrosine",
            "(seed)",
            "glutamic acid",
            "msm (methylsulfonylmethane)",
            "rhizome",
            "(leaf)",
            "other ingredients: rice flour",
            "proprietary formula",
            "whole plant",
            "glucosamine sulfate",
            "aerial parts",
            "bulb",
            "iron (as ferrous fumarate)",
            "l glutamine",
            "fd and c red 40",
            "oil",
            "natural strawberry flavor",
            "natural flavor.",
            "natural lemon flavor.",
            "thiamine (as thiamine hcl)",
            "phosphatidylserine",
            "propylene glycol",
            "other ingredients: tapioca syrup",
            "calcium silicate",
            "selenium (as l-selenomethionine)",
            "fruiting body",
            "extract.",
            "amazing grass green food blend",
            "sodium bicarbonate",
            "pantothenic acid (as d-calcium pantothenate)",
            "maltitol",
            "boswellia extract",
            "titanium dioxide.",
            "cellulose (plant origin)",
            "other ingredients: maltodextrin",
            "other ingredients: citric acid",
            "natural blueberry flavor",
            "leaf extract",
            "berry",
            "probiotic blend",
            "other ingredients: cane sugar",
            "calcium (dicalcium phosphate)",
            "rice maltodextrin",
            "natural flavors.",
            "flavors.",
            "glucose syrup (corn syrup)",
            "other ingredients: natural flavor",
            "niacin (vitamin b3)",
            "ascorbyl palmitate.",
            "extract (root)",
            "organic maltodextrin",
            "whole herb",
            "starch",
            "phenylethylamine hcl",
            "sucralose.",
            "organic evaporated cane sugar",
            "proprietary probiotic blend",
            "ginger flavor.",
            "fd and c blue 1.",
            "modified food starch",
            "organic sunflower oil (contains carnauba wax)",
            "manganese (as manganese amino acid chelate)",
            "other ingredients: vegetable cellulose.",
            "other ingredients: cellulose",
            "glucose syrup (tapioca syrup)",
            "proprietary blend:",
            "as ascorbic acid",
            "vegetable stearate.",
            "natural color",
            "chromium (as chromium nicotinate glycinate chelate)",
            "and silica.",
            "natural flavoring.",
            "paprika oleoresin (color)",
            "stearic acid (vegetable source)",
            "cornstarch.",
            "sugar (from beets)",
            "fruit and vegetable juice (for color)",
            "tapioca dextrin",
            "less than 2% of: citric acid",
            "4:1 extract",
            "phosphorus",
            "seeds",
            "stem",
            "magnesium stearate and silicon dioxide.",
            "cholecalciferol",
            "crospovidone",
            "glucomannan",
            "other ingredients: maltitol",
            "fruit pectin mix",
            "vegetable glycerin.",
            "zinc (as zinc citrate usp)",
            "magnesium stearate and silica.",
            "dextrin",
            "natural passion fruit flavor",
            "(herb)",
            "natural lemon flavor",
            "(bark)",
            "pyridoxine hydrochloride",
            "fructooligosaccharides",
            "calcium salt",
            "cellulose.",
            "malic acid.",
            "3",
            "potassium (as potassium citrate)"


    );

    public static void main(String[] args) throws IOException {
        File file = new File("D:\\work\\project");
        File[] files = file.listFiles();
        for (File file1 : files) {
            if (file1.getName().equals("runresult")) {
                continue;
            }
            File[] files1List = file1.listFiles();
            if (files1List == null) {
                continue;
            }
            String name = file1.getName();
            for (File listFile : files1List) {
                if (listFile.getName().endsWith("xls")) {
                    System.out.println("RUN :" + listFile.getPath());
                    run(listFile, name);
                }
            }
        }
//        File file = new File("D:\\work\\project\\美国\\免疫=美国2019-2021.xls");
//        run(file, "other");
    }

    @SneakyThrows
    private static void run(File file, String dir) {
        String name = file.getName();
        name = FilenameUtils.getBaseName(name);
        File rsFile = new File("D:\\work\\project\\runresult\\" + dir + "\\" + name + "_result.xls");
        if (rsFile.exists()) {
            System.out.println("RUN : exists " + file.getPath());
            return;
        }

        ReadResult<Map<String, Object>> result = PoiUtils.readAsMap(file, ExcelReadInfo.readInfo());
        List<Set<String>> list = result.getData().stream().filter(row -> row.get("Ingredients") != null)
                .map(row -> (String) row.get("Ingredients"))
                .map(t -> new HashSet<>(split(t)))
                .collect(Collectors.toList());
//        Map<String, Integer> map = collect.stream().collect(Collectors.groupingBy(t -> t, Collectors.reducing(0, t -> 1, Integer::sum)));
//        List<TextCount> data = map.entrySet().stream().map(entry -> new TextCount(entry.getKey(), entry.getValue())).collect(Collectors.toList());
        System.out.println("RUN :" + list.stream().mapToLong(Collection::size).sum());
        Apriori apriori = new Apriori(list, 0.1, 0.1);
        Map<Integer, Map<Set<String>, Integer>> allFrequentMap = apriori.allFrequentMap;

        HSSFWorkbook workbook = null;
        for (int i = 0; i < 5; i++) {
            Map<Set<String>, Integer> map = allFrequentMap.get(i);
            if (map != null && map.size() > 0) {
                List<Set<String>> keySet = new ArrayList<>(map.keySet());
                List<String> keys = keySet.stream().flatMap(Collection::stream).collect(Collectors.toList());
                List<String> trans = trans(keys);
                ArrayList<Set<String>> transList = new ArrayList<>();
                for (int j = 0; j < trans.size(); j = j + i + 1) {
                    HashSet<String> vals = new HashSet<>();
                    for (int k = 0; k < i + 1; k++) {
                        int index = j + k;
                        String e = trans.get(index);
                        vals.add(e);
                    }
                    transList.add(vals);
                }
                HashMap<Set<String>, Set<String>> transMap = new HashMap<>();
                for (int j = 0; j < keySet.size(); j++) {
                    transMap.put(keySet.get(j), transList.get(j));
                }
                List<TextCount> collect = map.entrySet().stream().map(entry -> {
                    Set<String> key = entry.getKey();
                    Set<String> transVal = transMap.get(key);
                    return new TextCount(String.join(",", key), String.join(",", transVal), entry.getValue());
                }).sorted(Comparator.comparingInt(TextCount::getCount).reversed()).collect(Collectors.toList());
                String sheetName = String.valueOf(i + 1);
                if (workbook == null) {
                    workbook = PoiUtils.createSheet(collect, sheetName);
                } else {
                    PoiUtils.createSheet(workbook, collect, sheetName);
                }
            }
        }


        if (workbook != null) {
            workbook.write(rsFile);
        }
    }

    private static List<String> split(String text) {
        List<String> texts = new ArrayList<>();
        char split = ',';
        int needClose = 0;
        char[] chars = text.toCharArray();
        int start = 0;
        int openIdx = 0;
        for (int i = 1; i < chars.length; i++) {
            if (i < start) {
                continue;
            }
            char c = chars[i];
            if (needClose > 0) {
                if (c == ')') {
                    needClose--;
                    if (needClose == 0) {
                        int endIndex = i + 1;
                        String mid = text.substring(start, i + 1);
                        if (mid.contains(",")) {
                            String substring = text.substring(start, openIdx).toLowerCase().trim();
                            if (!filter(substring)) {
                                texts.add(substring);
                            }
                            String[] split2 = text.substring(openIdx + 1, endIndex - 1).split(",");
                            for (String s : split2) {
                                s = s.toLowerCase().trim();
                                if (!filter(s)) {
                                    texts.add(s);
                                }
                            }
                        } else if (start != openIdx) {
                            mid = mid.toLowerCase().trim();
                            if (!filter(mid)) {
                                texts.add(mid);
                            }
                        } else {
                            mid = mid.substring(1, mid.length() - 1).toLowerCase().trim();
                            if (!filter(mid)) {
                                texts.add(mid);
                            }
                        }
                        start = endIndex;
                    }
                }
            }
            if (c == '(') {
                if (needClose == 0) {
                    openIdx = i;
                }
                needClose++;
                continue;
            }
            if (needClose > 0) {
                continue;
            }
            if (i == chars.length - 1) {
                String textf = new String(chars, start, i - start + 1).toLowerCase().trim();
                if (!filter(textf)) {
                    texts.add(textf);
                }
            } else if (c == split) {
                String textf = new String(chars, start, i - start).toLowerCase().trim();
                if (!filter(textf)) {
                    texts.add(textf);
                }
                start = i + 1;
            }
        }
        return texts;
    }

    private static boolean filter(String text) {
        if (StringUtils.isEmpty(text) || text.equals("cystine (l-)") || filters.contains(text) ||
                text.startsWith("vitamin") || text.contains("water") || text.contains("capsule")) {
            return true;
        }
        return false;
    }

    private static List<String> trans(List<String> text) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("sourceLanguage", "en");
        map.put("targetLanguage", "zh");
        map.put("type", 1);

        if (text.size() > 1000) {
            ArrayList<String> transRs = new ArrayList<>();
            ArrayList<String> texts0 = new ArrayList<>();
            for (String s : text) {
                texts0.add(s);
                if (texts0.size() == 1000) {
                    map.put("texts", texts0);
                    HttpUtils.addHeader("aiAppId", "ai_risk");
                    BaseResponse<JSONObject> response = HttpUtils.postForObjectThrowFail("http://localhost:10881/v1/in", map, new ParameterizedTypeReference<BaseResponse<JSONObject>>() {
                    });
                    List<String> collect = response.getData().getJSONArray("texts")
                            .toJavaList(JSONObject.class).stream().map(e -> e.getString("target"))
                            .collect(Collectors.toList());
                    transRs.addAll(collect);
                    texts0 = new ArrayList<>();
                }
            }
            if (texts0.size() > 0) {
                map.put("texts", texts0);
                HttpUtils.addHeader("aiAppId", "ai_risk");
                BaseResponse<JSONObject> response = HttpUtils.postForObjectThrowFail("http://localhost:10881/v1/in", map, new ParameterizedTypeReference<BaseResponse<JSONObject>>() {
                });
                List<String> collect = response.getData().getJSONArray("texts")
                        .toJavaList(JSONObject.class).stream().map(e -> e.getString("target"))
                        .collect(Collectors.toList());
                transRs.addAll(collect);
            }
            return transRs;
        } else {
            map.put("texts", text);
            HttpUtils.addHeader("aiAppId", "ai_risk");
            BaseResponse<JSONObject> response = HttpUtils.postForObjectThrowFail("http://localhost:10881/v1/in", map, new ParameterizedTypeReference<BaseResponse<JSONObject>>() {
            });
            return response.getData().getJSONArray("texts")
                    .toJavaList(JSONObject.class).stream().map(e -> e.getString("target"))
                    .collect(Collectors.toList());
        }

    }

    @Data
    @ExcelHead
    private static class TextCount {
        @ExcelCell(name = "Ingredient")
        private String text;
        @ExcelCell(name = "trans")
        private String trans;
        @ExcelCell(name = "Count")
        private Integer count;

        public TextCount(String text, String trans, Integer count) {
            this.text = text;
            this.trans = trans;
            this.count = count;
        }
    }

}
